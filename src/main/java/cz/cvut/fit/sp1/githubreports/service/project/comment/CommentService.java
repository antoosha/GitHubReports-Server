package cz.cvut.fit.sp1.githubreports.service.project.comment;

import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.NoEntityFoundException;
import cz.cvut.fit.sp1.githubreports.dao.project.CommentJpaRepository;
import cz.cvut.fit.sp1.githubreports.model.project.Comment;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.logging.Logger;

@AllArgsConstructor
@Service("CommentService")
public class CommentService implements CommentSPI {

    private final CommentJpaRepository repository;

    private static final Logger logger = Logger.getLogger(CommentService.class.getName());

    @Override
    public Collection<Comment> readAll() {
        return repository.findAll();
    }

    @Override
    public Comment readById(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> {
                    logger.warning("Comment with id " + id + " does not exist.");
                    return new NoEntityFoundException("Comment with id " + id + " does not exist.");
                });
    }

    @Override
    public Comment create(Comment comment) throws EntityStateException {
        if (comment.getCommentId() != null && repository.existsById(comment.getCommentId())){
            logger.warning("Comment with id " + comment.getCommentId() + " already exists.");
            throw new EntityStateException("Comment with id " + comment.getCommentId() + " already exists.");
        }
        comment.setCreatedDate(LocalDateTime.now());
        comment.setAuthorUsername(comment.getAuthor().getUsername());
        return repository.save(comment);
    }

    @Override
    public Comment update(Long id, Comment commentToUpdate) throws EntityStateException {
        Comment comment = readById(id);
        comment.setText(commentToUpdate.getText());
        comment.setIsEdited(true);
        return repository.save(comment);
    }

    @Override
    public void delete(Long id) {
        if (repository.existsById(id))
            repository.deleteById(id);
        else {
            logger.warning("Comment with id " + id + " does not exist.");
            throw new NoEntityFoundException("Comment with id " + id + " does not exist.");
        }
    }
}
