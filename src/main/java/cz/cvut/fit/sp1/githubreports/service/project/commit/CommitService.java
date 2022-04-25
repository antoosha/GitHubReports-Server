package cz.cvut.fit.sp1.githubreports.service.project.commit;

import cz.cvut.fit.sp1.githubreports.dao.project.CommitJpaRepository;
import cz.cvut.fit.sp1.githubreports.model.project.Commit;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@AllArgsConstructor
@Service("CommitService")
public class CommitService implements CommitSPI {

    CommitJpaRepository repository;

    @Override
    public Collection<Commit> readAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Commit> readById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void create(Commit commit) {
        repository.save(commit);
    }

    @Override
    public void update(Long id, Commit commit) {
        if (repository.existsById(id))
            repository.save(commit);
    }

    @Override
    public void delete(Long id) {
        if (repository.existsById(id))
            repository.deleteById(id);
    }
}
