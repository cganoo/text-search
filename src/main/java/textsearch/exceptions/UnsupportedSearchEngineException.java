package textsearch.exceptions;

/**
 * Thrown when an illegal CLI option is encountered.
 * Created by cganoo on 28/01/15.
 */
public class UnsupportedSearchEngineException extends SearchException {

    private static final long serialVersionUID = 1L;

    public UnsupportedSearchEngineException() {
        super();
    }

    public UnsupportedSearchEngineException(String message) {
        super(message);
    }

    public UnsupportedSearchEngineException(Throwable message) {
        super(message);
    }

    public UnsupportedSearchEngineException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
