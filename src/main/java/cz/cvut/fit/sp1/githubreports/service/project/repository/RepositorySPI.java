package cz.cvut.fit.sp1.githubreports.service.project.repository;

import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.model.project.Repository;

import java.util.Collection;
import java.util.Optional;

public interface RepositorySPI {

    Collection<Repository> readAll();

    Optional<Repository> readById(Long id);

    Repository create(Repository repository) throws EntityStateException;

    Repository update(Long id, Repository repository) throws EntityStateException;

    void delete(Long id);
}
