package elyra.parser.exception;

public class UnknownCommandException extends RuntimeException {
    public UnknownCommandException(String message) {
        super("Unknown command: " + message);
    }
}
