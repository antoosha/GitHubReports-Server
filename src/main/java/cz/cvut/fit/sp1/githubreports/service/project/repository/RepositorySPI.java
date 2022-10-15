package cz.cvut.fit.sp1.githubreports.service.project.repository;

import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.model.project.Repository;
import org.openapi.model.CommitSlimDTO;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;

public interface RepositorySPI {

    Collection<Repository> readAll();

    Repository readById(Long id);

    List<CommitSlimDTO> readCommitsByRepositoryId(Long id, Integer page, Integer size);

    Repository create(Repository repository, String tokenAuth) throws EntityStateException;

    Repository update(Long id, Repository repository) throws EntityStateException;

    void delete(Long id);

    Repository synchronize(Long id, String tokenAuth);
}
