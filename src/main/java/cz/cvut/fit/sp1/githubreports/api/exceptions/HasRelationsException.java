package cz.cvut.fit.sp1.githubreports.api.exceptions;

public class HasRelationsException extends RuntimeException {
    public HasRelationsException(String message) {
        super(message);
    }

    public HasRelationsException() {
        super("Has other relations.");
    }
}
