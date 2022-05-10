package cz.cvut.fit.sp1.githubreports.service.project.comment;

import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
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
    public Optional<Comment> readById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Comment create(Comment comment) throws EntityStateException {
        if (repository.existsById(comment.getCommentId())) throw new EntityStateException();
        comment.setCreatedDate(LocalDateTime.now());
        return repository.save(comment);
    }

    @Override
    public Comment update(Long id, Comment comment) throws EntityStateException {
        if (!repository.existsById(id)) throw new EntityStateException();
        return repository.save(comment);
    }

    @Override
    public void delete(Long id) {
        if (repository.existsById(id))
            repository.deleteById(id);
    }
}
