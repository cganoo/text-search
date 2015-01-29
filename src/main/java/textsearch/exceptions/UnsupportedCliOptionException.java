package textsearch.exceptions;

/**
 * Thrown when non-legal CLI options are encountered.
 * Created by cganoo on 28/01/15.
 */
public class UnsupportedCliOptionException extends CliException {

    private static final long serialVersionUID = 1L;

    public UnsupportedCliOptionException() {
        super();
    }

    public UnsupportedCliOptionException(String message) {
        super(message);
    }

    public UnsupportedCliOptionException(Throwable message) {
        super(message);
    }

    public UnsupportedCliOptionException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
