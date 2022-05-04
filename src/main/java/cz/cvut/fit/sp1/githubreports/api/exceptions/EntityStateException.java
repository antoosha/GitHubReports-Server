package cz.cvut.fit.sp1.githubreports.api.exceptions;

/**
 * A checked exception indicating problem related to existence of an entity.
 */
public class EntityStateException extends Exception {
    public EntityStateException() {
    }

    public <E> EntityStateException(E entity) {
        super("Illegal state of entity " + entity);
    }
}