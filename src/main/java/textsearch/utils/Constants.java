package textsearch.utils;

/**
 * Created by cganoo on 28/01/15.
 */
public class Constants {

    public static String OPTION_HELP_SHORT = "h";
    public static String OPTION_HELP_LONG = "help";
    public static String OPTION_SEARCH = "search";

    // TODO: Explain brittleness of cmdline argument separator
    public static int SEARCH_ARG_NUMBER = 4;
    public static final char SEARCH_ARG_SEPARATOR = ' ';

    public static final String PATTERN_DOCUMENT_DELIMITERS = "(?<=:|;|!)";
    public static final String PATTERN_QUERY_DELIMITERS = "( )";

    public static final String SEARCH_ENGINE_DEFAULT = "default";
    public static final String SEARCH_ENGINE_LUCENE = "lucene";

    public static final String HIGHLIGHT_STRATEGY_INTERPOLATED = "interpolated";
    public static final String HIGHLIGHT_STRATEGY_DISTINCT = "distinct";

    public static final String DOC_ID = "id";
    public static final String DOC_CONTENT = "content";
    public static final int HITS_PER_PAGE = 10;
}
