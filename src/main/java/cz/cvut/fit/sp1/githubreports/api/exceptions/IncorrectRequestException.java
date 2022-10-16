package cz.cvut.fit.sp1.githubreports.api.exceptions;

public class IncorrectRequestException extends RuntimeException {
    public IncorrectRequestException(String message) {
        super(message);
    }

    public IncorrectRequestException() {
        super("Bad request.");
    }
}
