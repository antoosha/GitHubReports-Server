package cz.cvut.fit.sp1.githubreports.service.project.repository;

import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.dao.project.RepositoryJpaRepository;
import cz.cvut.fit.sp1.githubreports.model.project.Repository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@AllArgsConstructor
@Service("RepositoryService")
public class RepositoryService implements RepositorySPI {

    RepositoryJpaRepository jpaRepository;

    @Override
    public Collection<Repository> readAll() {
        return jpaRepository.findAll();
    }

    @Override
    public Optional<Repository> readById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Repository create(Repository repository) throws EntityStateException {
        if (jpaRepository.existsById(repository.getRepositoryId()))
            throw new EntityStateException();
        return jpaRepository.save(repository);
    }

    @Override
    public Repository update(Long id, Repository repository) throws EntityStateException {
        if (!jpaRepository.existsById(id))
            throw new EntityStateException();
        return jpaRepository.save(repository);
    }

    @Override
    public void delete(Long id) {
        if (jpaRepository.existsById(id))
            jpaRepository.deleteById(id);
    }
}
