package textsearch.exceptions;

/**
 * Created by cganoo on 28/01/15.
 */
public class CliException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public CliException() {
    }

    public CliException(String message) {
        super(message);
    }

    public CliException(Throwable ex) {
        super(ex);
    }

    public CliException(String message, Throwable ex) {
        super(message, ex);
    }

}