package textsearch.api;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import textsearch.utils.Constants;
import textsearch.utils.Interpreter;

import java.util.*;

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

    /**
     *
     * @param document
     * @param query
     * @return
     */
    public String generateSnippets(final String searchEngine, final String document, final String query) {

        log.info("Generating relevant match ...");

        final List<Integer> matchIndex = Lists.newArrayList();

        /* Get the token map for the specified document */
        final Map<Integer, String> tokenMap = tokenizer.getTokenMap(document, query);

        /* Search individual query tokens in the document token map */
        final CharSequence[] queryCharSequence = tokenizer.tokenizeAsCharSeq(query, Constants.PATTERN_QUERY_DELIMITERS);
        for(final Map.Entry<Integer, String> e : tokenMap.entrySet()) {
            for(final CharSequence cs : queryCharSequence) {
                if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(e.getValue(), cs)) {
                    /* Accumulate the matched document token indexes */
                    matchIndex.add(e.getKey());
                }
            }
        }

        /* Delegate to the interpreter for choosing a relevance strategy */
        return interpreter.interpret(matchIndex, tokenMap);
    }
}
