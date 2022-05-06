package cz.cvut.fit.sp1.githubreports.service.project.commit;

import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.NoEntityFoundException;
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

    private void checkValidation(Commit commit) {
        if (commit.getRepository() == null)
            throw new EntityStateException();
        if (commit.getLoginAuthor().isEmpty()) throw new EntityStateException();
    }

    @Override
    public Collection<Commit> readAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Commit> readById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Commit create(Commit commit) throws EntityStateException {
        if (commit.getCommitId() != null) {
            if (repository.existsById(commit.getCommitId()))
                throw new EntityStateException();
        }
        checkValidation(commit);
        return repository.save(commit);
    }

    @Override
    public Commit update(Long id, Commit commit) throws EntityStateException {
        if (id == null || !repository.existsById(id))
            throw new NoEntityFoundException();
        checkValidation(commit);
        return repository.save(commit);
    }

    @Override
    public void delete(Long id) {
        if (repository.existsById(id))
            repository.deleteById(id);
    }
}
