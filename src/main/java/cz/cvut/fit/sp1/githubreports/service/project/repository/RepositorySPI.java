package cz.cvut.fit.sp1.githubreports.service.project.repository;

import cz.cvut.fit.sp1.githubreports.model.project.Repository;

import java.util.Collection;
import java.util.Optional;

public interface RepositorySPI {

    Collection<Repository> readRepositories();

    Optional<Repository> readRepositoryById(Long id);

    void createRepository(Repository repository);

    void updateRepository(Long id, Repository repository);

    void deleteRepository(Long id);
}
