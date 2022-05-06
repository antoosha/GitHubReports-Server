package cz.cvut.fit.sp1.githubreports.service.project.comment;

import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.model.project.Comment;

import java.util.Collection;
import java.util.Optional;

public interface CommentSPI {

    Collection<Comment> readAll();

    Optional<Comment> readById(Long id);

    Comment create(Comment comment) throws EntityStateException;

    Comment update(Long id, Comment comment) throws EntityStateException;

    void delete(Long id);
}
