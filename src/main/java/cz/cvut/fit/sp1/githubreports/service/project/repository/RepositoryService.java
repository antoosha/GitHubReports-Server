package cz.cvut.fit.sp1.githubreports.service.project.repository;

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
    public Collection<Repository> readRepositories() {
        return jpaRepository.findAll();
    }

    @Override
    public Optional<Repository> readRepositoryById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public void createRepository(Repository repository) {
        jpaRepository.save(repository);
    }

    @Override
    public void updateRepository(Long id, Repository repository) {
        if (jpaRepository.existsById(id))
            jpaRepository.save(repository);
    }

    @Override
    public void deleteRepository(Long id) {
        if (jpaRepository.existsById(id))
            jpaRepository.deleteById(id);
    }
}
