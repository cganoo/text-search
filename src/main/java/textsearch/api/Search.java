package textsearch.api;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import textsearch.exceptions.SearchException;
import textsearch.exceptions.UnsupportedSearchEngineException;
import textsearch.utils.Constants;
import textsearch.utils.Interpreter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by cganoo on 28/01/15.
 */
@Controller
public class Search {
    final static Logger log = LoggerFactory.getLogger(Search.class);

    @Autowired
    private Tokenizer tokenizer;

    @Autowired
    private Interpreter interpreter;

    @Autowired
    private IndexWriter indexWriter;

    @Autowired @Lazy
    private IndexSearcher indexSearcher;

    @Autowired
    private QueryParser queryParser;

    /**
     * @param document
     * @param query
     * @return
     */
    public String generateSnippets(final String searchEngine, final String highlightStrategy, final String document, final String query) throws SearchException {

        /*
         * The parse method in Cli class has input validation.
         * Including here again for better unit test coverage
         */
        if (StringUtils.isEmpty(searchEngine)
                || StringUtils.isEmpty(highlightStrategy)
                || StringUtils.isEmpty(document)
                || StringUtils.isEmpty(query)) {
            throw new IllegalArgumentException("Encountered malformed input. Please check your arguments again");
        }

        log.info("Tokenizing specified document ...");

         /* Create the token map for the specified document */
        final Map<Integer, String> tokenMap = tokenizer.getTokenMap(document);

        final List<Integer> matchIndex;

        log.info("Adjusting textsearch.search strategy based on the specified searchEngine ...");
        switch (searchEngine) {
            case Constants.SEARCH_ENGINE_DEFAULT:
                matchIndex = defaultSearch(tokenMap, query);
                break;
            case Constants.SEARCH_ENGINE_LUCENE:
                matchIndex = luceneSearch(tokenMap, query);
                break;
            default:
                throw new UnsupportedSearchEngineException("Specified searchEngine [" + searchEngine + "] is not supported");
        }

        /* Delegate to the interpreter for choosing a relevance strategy */
        return interpreter.interpret(highlightStrategy, matchIndex, tokenMap);
    }

    /**
     * @param tokenMap
     * @param query
     * @return
     */
    public List<Integer> defaultSearch(final Map<Integer, String> tokenMap, final String query) {

        log.info("Generating relevant match ...");

        final List<Integer> matchIndex = Lists.newArrayList();

        /* Search individual query tokens in the document token map */
        final CharSequence[] queryCharSequence = tokenizer.tokenizeAsCharSeq(query, Constants.PATTERN_QUERY_DELIMITERS);
        for (final Map.Entry<Integer, String> e : tokenMap.entrySet()) {
            for (final CharSequence cs : queryCharSequence) {
                if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(e.getValue(), cs)) {
                    /* Accumulate the matched document token indexes */
                    matchIndex.add(e.getKey());
                }
            }
        }
        return matchIndex;
    }

    /**
     * @param tokenMap
     * @param query
     * @return
     * @throws SearchException
     */
    public List<Integer> luceneSearch(final Map<Integer, String> tokenMap, final String query) throws SearchException {

        log.info("Generating relevant match using lucene ...");

        try {
            /* Create index from the document */
            for (final Map.Entry<Integer, String> e : tokenMap.entrySet()) {
                indexDoc(indexWriter, String.valueOf(e.getKey()), e.getValue());
            }

            /*
             * Try to commit and close the writer
             */
            try {
                indexWriter.commit();
                indexWriter.close();

                /* Search for the query */
                final List<Integer> matchIndex = search(indexSearcher, query);
                return matchIndex;
            } catch (NullPointerException e) {
                /*
                 * If index writes fail, then segments* file will not be available in RAMDirectory
                 * This will cause indexSearcher bean creation to fail.
                 * If this occurs, indicate the error and ask user to try 'default' textsearch.search engine
                 */
                log.error("Encountered NPE when committing/closing lucene indexWriter. Please try using 'default' textsearch.search engine instead of lucene ...");
                throw new SearchException("Index writes failed to RAMDirectory. Please try using 'default' textsearch.search engine instead");
            }
        } catch (IOException | ParseException e) {
            log.error("Encountered exception when searching with lucene [{}]", e.getMessage());
            throw new SearchException(e);
        }
    }

    /**
     * @param w
     * @param id
     * @param content
     * @throws IOException
     */
    private void indexDoc(IndexWriter w, String id, String content) throws IOException {
        Document doc = new Document();
        doc.add(new Field(Constants.DOC_ID, id, Field.Store.YES, Field.Index.NO));
        doc.add(new Field(Constants.DOC_CONTENT, content, Field.Store.YES, Field.Index.ANALYZED));
        w.addDocument(doc);
    }

    /**
     * Searches for the given string in the "content" field
     */
    private List<Integer> search(IndexSearcher searcher, String queryString)
            throws ParseException, IOException {

        final List<Integer> docIdList = Lists.newArrayList();
        Query query = queryParser.parse(queryString);

        TopScoreDocCollector collector = TopScoreDocCollector.create(Constants.HITS_PER_PAGE, false);
        searcher.search(query, collector);

        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        int hitCount = collector.getTotalHits();
        log.info("Lucene Logs: textsearch.search hitCount: {}", hitCount);

        /* Examine the Hits object to see if there were any matches */

        if (hitCount == 0) {
            log.info("No matches found for queryString [{}]", queryString);
        } else {
            log.info("Hits for queryString [{}] found!", queryString);

            /* Iterate over the Documents in the Hits object */
            for (int i = 0; i < hitCount; i++) {
                ScoreDoc scoreDoc = hits[i];
                int docId = scoreDoc.doc;
                float docScore = scoreDoc.score;

                Document doc = searcher.doc(docId);

                /* Print the id and content fields */
                log.info("Lucene Logs: id: {}, content: {}, score:{}", doc.get(Constants.DOC_ID), doc.get(Constants.DOC_CONTENT), docScore);

                docIdList.add(Integer.valueOf(doc.get(Constants.DOC_ID)));
            }
        }
        log.info("Lucene Logs: doc ids = {}", docIdList);
        return docIdList;
    }
}
