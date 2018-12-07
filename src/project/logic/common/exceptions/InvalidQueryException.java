package project.logic.common.exceptions;

public class InvalidQueryException extends ProjectRuntimeException {
    public InvalidQueryException(String message) {
        super(message);
    }

    public InvalidQueryException(Throwable throwable) {
        super(throwable);
    }

    public InvalidQueryException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
