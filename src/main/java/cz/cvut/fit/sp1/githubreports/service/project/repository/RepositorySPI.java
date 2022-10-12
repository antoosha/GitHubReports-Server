package cz.cvut.fit.sp1.githubreports.service.project.repository;

import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.model.project.Repository;

import java.util.List;

public interface RepositorySPI {

    List<Repository> readAll();

    Repository readById(Long id);

    Repository create(Repository repository, String tokenAuth) throws EntityStateException;

    Repository update(Long id, Repository repository, String tokenAuth) throws EntityStateException;

    void delete(Long id);

    Repository synchronize(Long id, String tokenAuth);
}
