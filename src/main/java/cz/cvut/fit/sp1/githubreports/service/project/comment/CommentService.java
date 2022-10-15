package cz.cvut.fit.sp1.githubreports.service.project.comment;

import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.NoEntityFoundException;
import cz.cvut.fit.sp1.githubreports.dao.project.CommentJpaRepository;
import cz.cvut.fit.sp1.githubreports.model.project.Comment;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@AllArgsConstructor
@Service("CommentService")
public class CommentService implements CommentSPI {

    private final CommentJpaRepository repository;

    @Override
    public Collection<Comment> readAll() {
        return repository.findAll();
    }

    @Override
    public Comment readById(Long id) {
        return repository.findById(id).orElseThrow(NoEntityFoundException::new);
    }

    @Override
    public Comment create(Comment comment) throws EntityStateException {
        if (comment.getCommentId() != null && repository.existsById(comment.getCommentId()))
            throw new EntityStateException();
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
        else throw new NoEntityFoundException();
    }
}
