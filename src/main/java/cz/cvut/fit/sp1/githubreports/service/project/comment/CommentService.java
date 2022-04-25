package cz.cvut.fit.sp1.githubreports.service.project.comment;

import cz.cvut.fit.sp1.githubreports.dao.project.CommentJpaRepository;
import cz.cvut.fit.sp1.githubreports.model.project.Comment;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@AllArgsConstructor
@Service("CommentService")
public class CommentService implements CommentSPI {

    CommentJpaRepository repository;

    @Override
    public Collection<Comment> readAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Comment> readById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void create(Comment comment) {
        repository.save(comment);
    }

    @Override
    public void update(Long id, Comment comment) {
        if (repository.existsById(id))
            repository.save(comment);
    }

    @Override
    public void delete(Long id) {
        if (repository.existsById(id))
            repository.deleteById(id);
    }
}
