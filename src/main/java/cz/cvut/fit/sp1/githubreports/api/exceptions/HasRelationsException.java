package cz.cvut.fit.sp1.githubreports.api.exceptions;

public class HasRelationsException extends RuntimeException {
    public HasRelationsException() {
        super();
    }

    public HasRelationsException(String errorMessage) {
        super(errorMessage);
    }
}
