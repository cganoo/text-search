package textsearch.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import textsearch.exceptions.NoMatchFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by cganoo on 28/01/15.
 */
@Controller
public class Interpreter {
    final static Logger log = LoggerFactory.getLogger(Interpreter.class);

    public String interpret(final String highlightStrategy, final List<Integer> matchIndex, Map<Integer, String> tokenMap) {

        final StringBuilder sb = new StringBuilder();

        /* No match found for specified query */
        if (matchIndex == null || matchIndex.isEmpty()) {
            log.info("No match found for the query string");
            /*
             * Depending on how this module is to be consumed, throwing an exception might be reasonable.
             * Other strategies might be to indicate a default message that no match was found.
             * Alternatively the entire original document could be returned,
             * leaving it to the user/downstream system to exercise its own judgement.
             *
             * Here, we simply choose to throw an exception.
             */
            throw new NoMatchFoundException("Could not find any relevant match for the specified document and query");
        } else if (matchIndex.size() == 1) {
            /* Precisely one document token contained the query. Just return that match */
            sb.append(tokenMap.get(matchIndex.get(0)));
        } else {
            /*
             * Highlight strategy governs what portions of the matched document are returned as 'relevant' match.
             * Here multiple document tokens matched the query:
             *  - interpolated: Return ALL document tokens in the range indicated by the matched tokens (inclusive)
             *  - distinct: Return only those document tokens indicated by the matched tokens
             */
            switch(highlightStrategy) {
                case Constants.HIGHLIGHT_STRATEGY_INTERPOLATED:
                    for (int i = Collections.min(matchIndex); i <= Collections.max(matchIndex); i++) {
                        sb.append(tokenMap.get(i));
                        sb.append(" ");
                    }
                    break;
                case Constants.HIGHLIGHT_STRATEGY_DISTINCT:
                    for (int i : matchIndex) {
                        sb.append(tokenMap.get(i));
                        sb.append(" ");
                    }
                    break;
            }

        }
        return StringUtils.trim(sb.toString());
    }
}
