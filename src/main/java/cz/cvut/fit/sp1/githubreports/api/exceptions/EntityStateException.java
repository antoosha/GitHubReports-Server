package cz.cvut.fit.sp1.githubreports.api.exceptions;

/**
 * A checked exception indicating problem related to existence of an entity.
 */
public class EntityStateException extends RuntimeException {
    public EntityStateException(String message) {
        super(message);
    }

    public EntityStateException() {
        super("Illegal state of entity.");
    }
}