package textsearch.utils;

/**
 * A pool to hold constant values used in the application
 * Created by cganoo on 28/01/15.
 */
public class Constants {

    /* CLI */
    public static String OPTION_HELP_SHORT = "h";
    public static String OPTION_HELP_LONG = "help";
    public static String OPTION_SEARCH = "search";

    public static int SEARCH_ARG_NUMBER = 4;
    public static final char SEARCH_ARG_SEPARATOR = ' ';

    /* Tokenizer patterns */
    public static final String PATTERN_DOCUMENT_DELIMITERS = "(?<=:|;|!)";
    public static final String PATTERN_QUERY_DELIMITERS = "( )";

    /* Search Engines */
    public static final String SEARCH_ENGINE_DEFAULT = "default";
    public static final String SEARCH_ENGINE_LUCENE = "lucene";

    /* Highlight strategies */
    public static final String HIGHLIGHT_STRATEGY_INTERPOLATED = "interpolated";
    public static final String HIGHLIGHT_STRATEGY_DISTINCT = "distinct";

    /* Lucene parameters */
    public static final String DOC_ID = "id";
    public static final String DOC_CONTENT = "content";
    public static final int HITS_PER_PAGE = 10;
}
