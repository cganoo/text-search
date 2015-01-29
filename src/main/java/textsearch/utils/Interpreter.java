package textsearch.utils;

import org.springframework.stereotype.Controller;
import com.amazonaws.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import textsearch.exceptions.NoMatchFoundException;

import java.util.*;

/**
 * Created by cganoo on 28/01/15.
 */
@Controller
public class Interpreter {
    final static Logger log = LoggerFactory.getLogger(Interpreter.class);

    public String interpret(final List<Integer> matchIndex, Map<Integer, String> tokenMap) {

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
            /* Multiple document tokens matched the query.
             * Return ALL document tokens in the range indicated by the matched tokens (inclusive)
             * */
            for (int i = Collections.min(matchIndex); i <= Collections.max(matchIndex); i++) {
                sb.append(tokenMap.get(i));
                sb.append(" ");
            }
        }
        return StringUtils.trim(sb.toString());
    }
}
