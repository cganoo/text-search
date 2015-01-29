package textsearch.exceptions;

/**
 * Created by cganoo on 28/01/15.
 */
public class SearchException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public SearchException() {
    }

    public SearchException(String message) {
        super(message);
    }

    public SearchException(Throwable ex) {
        super(ex);
    }

    public SearchException(String message, Throwable ex) {
        super(message, ex);
    }

}