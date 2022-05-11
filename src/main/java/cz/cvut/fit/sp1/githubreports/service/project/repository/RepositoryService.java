package cz.cvut.fit.sp1.githubreports.service.project.repository;

import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.IncorrectRequestException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.NoEntityFoundException;
import cz.cvut.fit.sp1.githubreports.dao.project.ProjectJpaRepository;
import cz.cvut.fit.sp1.githubreports.dao.project.RepositoryJpaRepository;
import cz.cvut.fit.sp1.githubreports.model.project.Commit;
import cz.cvut.fit.sp1.githubreports.model.project.Repository;
import cz.cvut.fit.sp1.githubreports.service.project.commit.CommitSPI;
import lombok.AllArgsConstructor;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

@AllArgsConstructor
@Service("RepositoryService")
public class RepositoryService implements RepositorySPI {

    RepositoryJpaRepository jpaRepository;
    ProjectJpaRepository projectJpaRepository;
    CommitSPI commitSPI;

    private void checkValidation(Repository repository) {
        if (repository.getProject() == null)
            throw new EntityStateException();
        if (repository.getRepositoryURL().isEmpty())
            throw new EntityStateException();
    }

    private void checkSameRepositoryNames(Repository repository) {
        if (repository.getProject().getRepositories().stream()
                .anyMatch(rep -> rep.getRepositoryName().equals(repository.getRepositoryName()))) {
            throw new EntityStateException();
        }
    }

    private void pullUpRepositoryFromGitHub(Repository repository, String tokenAuth) throws IncorrectRequestException, EntityStateException {
        String[] tokens = repository.getRepositoryURL().split("/");
        if (tokens.length != 5) {
            throw new IncorrectRequestException("Repository name must be in format owner/repo");
        }
        String formattedURL = tokens[3] + "/" + tokens[4]; //get owner + / + repo
        //check user's own name
        if (repository.getRepositoryName().isEmpty()) {
            repository.setRepositoryName(tokens[4]);
        }
        checkSameRepositoryNames(repository);

        try {
            GitHub gitHub = GitHub.connectUsingOAuth(tokenAuth);
            GHRepository ghRepository = gitHub.getRepository(formattedURL);
            repository = jpaRepository.save(repository); // saving without any commits in it
            pullUpCommitsFromGHRepository(repository, ghRepository);// pulling up commits and adding them to this repo
        } catch (IOException e) {
            throw new IncorrectRequestException(e.getMessage());
        }
    }

    private LocalDateTime convertToLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    private void pullUpCommitsFromGHRepository(Repository repository, GHRepository ghRepository) {
        try {
            ghRepository.listCommits().withPageSize(1).toList().forEach(
                    ghCommit -> {
                        try {
                            Commit commit = commitSPI.create(new Commit(null,
                                    convertToLocalDate(ghCommit.getCommitDate()),
                                    ghCommit.getSHA1(),
                                    ghCommit.getCommitShortInfo().getAuthor().getName(),
                                    ghCommit.getCommitShortInfo().getMessage(),
                                    jpaRepository.findRepositoryByRepositoryName(repository.getRepositoryName()),
                                    new ArrayList<>(),
                                    new ArrayList<>()));
                        } catch (IOException e) {
                            throw new EntityStateException(e.getMessage());
                        }
                    }
            );
        } catch (IOException e) {
            throw new EntityStateException(e.getMessage());
        }
    }

    @Override
    public Collection<Repository> readAll() {
        return jpaRepository.findAll();
    }

    @Override
    public Optional<Repository> readById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Repository create(Repository repository, String tokenAuth) throws EntityStateException {
        if (repository.getRepositoryId() != null) {
            if (jpaRepository.existsById(repository.getRepositoryId()))
                throw new EntityStateException();
        }
        checkValidation(repository);
        pullUpRepositoryFromGitHub(repository, tokenAuth);
        return jpaRepository.findRepositoryByRepositoryName(repository.getRepositoryName());
    }

    @Override
    public Repository update(Long id, Repository repository) throws EntityStateException {
        if (id == null || !jpaRepository.existsById(id))
            throw new NoEntityFoundException();
        checkValidation(repository);
        checkSameRepositoryNames(repository);
        return jpaRepository.save(repository);
    }

    @Override
    public void delete(Long id) {
        if (jpaRepository.existsById(id))
            jpaRepository.deleteById(id);
        else throw new EntityNotFoundException();
    }
}
