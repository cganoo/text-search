package textsearch.exceptions;

/**
 * Thrown when an illegal CLI option is encountered.
 * Created by cganoo on 28/01/15.
 */
public class NonreadableCliOptionException extends CliException {

    private static final long serialVersionUID = 1L;

    public NonreadableCliOptionException() {
        super();
    }

    public NonreadableCliOptionException(String message) {
        super(message);
    }

    public NonreadableCliOptionException(Throwable message) {
        super(message);
    }

    public NonreadableCliOptionException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
