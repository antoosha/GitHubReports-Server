package cz.cvut.fit.sp1.githubreports.service.project.comment;

import cz.cvut.fit.sp1.githubreports.model.project.Comment;

import java.util.Collection;
import java.util.Optional;

public interface CommentSPI {

    Collection<Comment> readAll();

    Optional<Comment> readById(Long id);

    void create(Comment comment);

    void update(Long id, Comment comment);

    void delete(Long id);
}
