package textsearch.api;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import textsearch.utils.Constants;

import java.util.List;
import java.util.Map;

/**
 * Created by cganoo on 28/01/15.
 */
@Controller
public class Tokenizer {
    final static Logger log = LoggerFactory.getLogger(Tokenizer.class);

    /**
     *
     * @param document
     * @return
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
     *
     * @param s
     * @param pattern
     * @return
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
     *
     * @param s
     * @return
     */
    public CharSequence[] convertListToCharSeq(final List<String> s) {
        if( s == null || s.isEmpty()) {
            return null;
        } else {
            return s.toArray(new CharSequence[s.size()]);
        }
    }

    /**
     *
     * @param s
     * @param pattern
     * @return
     */
    public List<String> tokenize(final String s, final String pattern) {
         final List<String> tokens = Lists.newArrayList(Splitter.onPattern(pattern)
                .trimResults()
                .omitEmptyStrings()
                .split(s));
        return tokens;
    }
}
