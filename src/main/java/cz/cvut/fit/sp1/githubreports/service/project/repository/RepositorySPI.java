package cz.cvut.fit.sp1.githubreports.service.project.repository;

import cz.cvut.fit.sp1.githubreports.model.project.Repository;

import java.util.Collection;
import java.util.Optional;

public interface RepositorySPI {

    Collection<Repository> readAll();

    Optional<Repository> readById(Long id);

    void create(Repository repository);

    void update(Long id, Repository repository);

    void delete(Long id);
}
