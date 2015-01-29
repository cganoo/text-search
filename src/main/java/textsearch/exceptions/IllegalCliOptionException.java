package textsearch.exceptions;

/**
 * Thrown when an illegal CLI option is encountered.
 * Created by cganoo on 28/01/15.
 */
public class IllegalCliOptionException extends CliException {

    private static final long serialVersionUID = 1L;

    public IllegalCliOptionException() {
        super();
    }

    public IllegalCliOptionException(String message) {
        super(message);
    }

    public IllegalCliOptionException(Throwable message) {
        super(message);
    }

    public IllegalCliOptionException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
