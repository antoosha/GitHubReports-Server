package cz.cvut.fit.sp1.githubreports.api.exceptions;

/**
 * An unchecked (runtime) exception indicating illegal operation (authorization denied).
 */
public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String s) {
        super(s);
    }
}