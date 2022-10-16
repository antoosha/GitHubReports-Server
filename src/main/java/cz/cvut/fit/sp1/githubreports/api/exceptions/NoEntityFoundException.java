package cz.cvut.fit.sp1.githubreports.api.exceptions;

public class NoEntityFoundException extends RuntimeException {
    public NoEntityFoundException(String message) {
        super(message);
    }

    public NoEntityFoundException() {
        super("No entity found.");
    }
}
