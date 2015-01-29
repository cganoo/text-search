package textsearch.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import textsearch.utils.Constants;

import java.io.IOException;

/**
 * A configuration class to define beans needed by the lucene search engine
 * Some lucene classes are marked deprecated but this has no critical impact for our usecase.
 * Created by cganoo on 28/01/15.
 */
@Configuration
public class LuceneConfig {

    final static Logger log = LoggerFactory.getLogger(LuceneConfig.class);

    @Bean(name = "ramDirectory")
    public Directory getRamDirectory() throws IOException {
        return new RAMDirectory();
    }

    @Bean(name = "analyzer")
    public Analyzer getAnalyzer() {
        return new StandardAnalyzer(Version.LUCENE_4_10_3);
    }

    @Bean(name = "indexWriter")
    @DependsOn("ramDirectory")
    public IndexWriter getIndexWriter() throws IOException {
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_3, getAnalyzer());
        IndexWriter writer = new IndexWriter(getRamDirectory(), iwc);
        return writer;
    }

    /*
     * Lazily instantiate the indexSearcher bean since it needs on the indexes created by indexWriter.
     * Normally these would be available for known documents.
     * However in our case the user supplies them at start time.
     */
    @Bean(name = "indexSearcher")
    @DependsOn("ramDirectory")
    @Lazy
    public IndexSearcher getIndexSearcher() throws IOException {
        IndexReader reader = IndexReader.open(getRamDirectory());
        return new IndexSearcher(reader);
    }

    @Bean(name = "queryParser")
    @DependsOn("analyzer")
    public QueryParser getQueryParser() throws IOException {
        return new QueryParser(Version.LUCENE_4_10_3, Constants.DOC_CONTENT, getAnalyzer());
    }

}
