package textsearch.utils;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

/**
 * Utility class to handle tokenization of documents and query strings
 * Created by cganoo on 28/01/15.
 */
@Controller
public class Tokenizer {
    final static Logger log = LoggerFactory.getLogger(Tokenizer.class);

    /**
     * Tokenizes specified document
     * @param document string to tokeninze
     * @return map of tokenized fragments and ids
     */
    public Map<Integer, String> getTokenMap(final String document) {
        final Map<Integer, String> tokenMap = Maps.newHashMap();

        /* Tokenize the document and query strings */
        final List<String> docToks = tokenize(document, Constants.PATTERN_DOCUMENT_DELIMITERS);

        /* Populate document tokens in the Tokenization map */
        for(int i = 0; i < docToks.size(); i++ ) {
            final String tokenValue = docToks.get(i);
            tokenMap.put((i + 1), tokenValue);
        }
        return tokenMap;
    }

    /**
     * Tokenizes a string and returns the tokens as a string list
     * @param s string to tokenize
     * @param pattern pattern to honor when tokenizing
     * @return list of string tokens
     */
    public List<String> tokenize(final String s, final String pattern) {
         final List<String> tokens = Lists.newArrayList(Splitter.onPattern(pattern)
                .trimResults()
                .omitEmptyStrings()
                .split(s));
        return tokens;
    }

    /**
     * Tokenizes a string and returns the tokens as a char sequence
     * @param s string to tokenize
     * @param pattern pattern to honor when tokenizing
     * @return char sequence of tokens
     */
    public CharSequence[] tokenizeAsCharSeq(final String s, final String pattern) {
        List<String> tokens = tokenize(s, pattern);
        if( tokens == null || tokens.isEmpty()) {
            return null;
        } else {
            return convertListToCharSeq(tokens);
        }
    }

    /**
     * Convenience method to convert a list of strings to a char sequence
     * @param s string to tokenize
     * @return char sequence of tokens
     */
    private CharSequence[] convertListToCharSeq(final List<String> s) {
        if( s == null || s.isEmpty()) {
            return null;
        } else {
            return s.toArray(new CharSequence[s.size()]);
        }
    }
}
