package textsearch.exceptions;

/**
 * Thrown when an illegal CLI option is encountered.
 * Created by cganoo on 28/01/15.
 */
public class NoMatchFoundException extends SearchException {

    private static final long serialVersionUID = 1L;

    public NoMatchFoundException() {
        super();
    }

    public NoMatchFoundException(String message) {
        super(message);
    }

    public NoMatchFoundException(Throwable message) {
        super(message);
    }

    public NoMatchFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
