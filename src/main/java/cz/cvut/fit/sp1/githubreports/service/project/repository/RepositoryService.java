package cz.cvut.fit.sp1.githubreports.service.project.repository;

import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.IncorrectRequestException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.NoEntityFoundException;
import cz.cvut.fit.sp1.githubreports.dao.project.CommitJpaRepository;
import cz.cvut.fit.sp1.githubreports.dao.project.ProjectJpaRepository;
import cz.cvut.fit.sp1.githubreports.dao.project.RepositoryJpaRepository;
import cz.cvut.fit.sp1.githubreports.model.project.Commit;
import cz.cvut.fit.sp1.githubreports.model.project.Project;
import cz.cvut.fit.sp1.githubreports.model.project.Repository;
import cz.cvut.fit.sp1.githubreports.service.project.commit.CommitMapper;
import cz.cvut.fit.sp1.githubreports.service.project.commit.CommitSPI;
import java.util.logging.Logger;
import lombok.AllArgsConstructor;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.openapi.model.CommitSlimDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service("RepositoryService")
public class RepositoryService implements RepositorySPI {

    private static final Logger logger = Logger.getLogger(RepositoryService.class.getName());
    
    private final RepositoryJpaRepository jpaRepository;

    private final CommitJpaRepository commitJpaRepository;

    private final CommitSPI commitSPI;

    private final CommitMapper commitMapper;

    @PersistenceContext
    private final EntityManager entityManager;

    private void checkValidation(Repository repository) {
        if (repository.getProject() == null) {
            logger.warning("Repository should be included in some project: " + repository);
            throw new EntityStateException("Repository should be included in some project: " + repository);
        }
        if (repository.getRepositoryURL().isEmpty()) {
            logger.warning("Repository should have some URL: " + repository);
            throw new EntityStateException("Repository should have some URL: " + repository);
        }
    }

    private void checkSameRepositoryNames(Repository repository) {
        if (repository.getProject().getRepositories().stream()
            .anyMatch(rep -> rep.getRepositoryName().equals(repository.getRepositoryName()))) {
            logger.warning(
                "Repository can't be included into a project which already has a repository with such name: "
                    + repository);
            throw new EntityStateException(
                "Repository can't be included into a project which already has a repository with such name: "
                    + repository);
        }
    }

    private String getFormattedURL(String repositoryURL) {
        String[] tokens = repositoryURL.split("/");
        if (tokens.length != 5) {
            logger.warning("Bad format, repository name must be in format owner/repo: " + repositoryURL);
            throw new IncorrectRequestException("Repository name must be in format owner/repo: " + repositoryURL);
        }
        return tokens[3] + "/" + tokens[4]; //get owner + / + repo
    }

    private void checkName(Repository repository, String newName) {
        //check user's own name
        if (repository.getRepositoryName().isEmpty()) {
            repository.setRepositoryName(newName);
        }
    }

    @Transactional
    public Repository pullUpRepositoryFromGitHub(String formattedURL, Repository repository, String tokenAuth)
        throws IncorrectRequestException, EntityStateException {
        try {
            GitHub gitHub = GitHub.connectUsingOAuth(tokenAuth);
            GHRepository ghRepository = gitHub.getRepository(formattedURL);
            repository = jpaRepository.save(repository); // for create no commits, for update existed commits
            pullUpCommitsFromGHRepository(repository, ghRepository);// pulling up commits and adding them to this
            entityManager.flush();
        } catch (IOException e) {
            logger.warning("An error occurred while pulling the repository from Github up: " + e);
            throw new IncorrectRequestException(
                "An error occurred while pulling the repository from Github up: " + e);
        }
        return repository;
    }

    private LocalDateTime convertToLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    private void pullUpCommitsFromGHRepository(Repository repository, GHRepository ghRepository) {
        try {
            List<Commit> oldCommits = repository.getCommits();
            List<GHCommit> ghCommits = ghRepository.listCommits().withPageSize(1).toList();

            /**
             * 1) If commit exists in our app and in GitHub then set deleted flag to false.
             * 2) If commit doesn't exist in our app and exists in GitHub then pull commits from GitHub.
             * 3) If commit exists in our app and doesn't exist in GitHub then change deleted flag to true.
             */
            //  3) case
            for (Commit oldCommit : oldCommits) {
                commitSPI.changeIsDeleted(oldCommit, ghCommits.stream()
                    .noneMatch(commit -> commit.getSHA1().equals(oldCommit.getHashHubId())));
            }

            for (GHCommit ghCommit : ghCommits) {
                // 1) 2) cases
                if (oldCommits.stream()
                    .noneMatch(commit -> commit.getHashHubId().equals(ghCommit.getSHA1()))) {
                    commitSPI.create(new Commit(null, convertToLocalDate(ghCommit.getCommitDate()),
                        ghCommit.getSHA1(), ghCommit.getCommitShortInfo().getAuthor().getName(),
                        ghCommit.getCommitShortInfo().getMessage(), false,
                        jpaRepository.findRepositoryByRepositoryName(repository.getRepositoryName())
                            .orElseThrow(() -> {
                                logger.warning("Can't find a repo with such name: " + repository);
                                return new EntityStateException(
                                    "Can't find a repo with such name: " + repository);
                            }), new ArrayList<>(), new ArrayList<>()));
                }
            }
        } catch (Exception e) {
            logger.severe("Couldn't retrieve information from GH repo: " + ghRepository + e);
            throw new EntityStateException(
                "Couldn't retrieve information from GH repo: " + ghRepository + e);
        }
    }

    @Override
    public Collection<Repository> readAll() {
        return jpaRepository.findAll();
    }

    @Override
    public Repository readById(Long id) {
        return jpaRepository.findById(id)
            .orElseThrow(() -> {
                logger.warning("Can't find a repo with id: " + id);
                return new NoEntityFoundException("Can't find a repo with id: " + id);
            });
    }

    @Override
    public List<CommitSlimDTO> readCommitsByRepositoryId(Long id, Integer page, Integer size) {
        Repository repository = readById(id);
        Pageable pageable = PageRequest.of(page, size);

        return commitJpaRepository.findAllByRepository(repository, pageable).stream().map(commitMapper::toSlimDTO).toList();
    }

    @Override
    @Transactional
    public Repository create(Repository repository, String tokenAuth) throws EntityStateException {
        if (repository.getRepositoryId() != null) {
            if (jpaRepository.existsById(repository.getRepositoryId())) {
                logger.warning("Repo with such id already exists: " + repository);
                throw new EntityStateException("Repo with such id already exists: " + repository);
            }
        }
        checkValidation(repository);
        String formattedURL = getFormattedURL(repository.getRepositoryURL());
        checkName(repository, formattedURL.split("/")[1]);
        checkSameRepositoryNames(repository);
        repository = pullUpRepositoryFromGitHub(formattedURL, repository, tokenAuth);
        entityManager.refresh(repository);
        return repository;
    }

    @Override
    @Transactional
    public Repository update(Long id, Repository repositoryToUpdate) throws EntityStateException {
        if (id == null || !jpaRepository.existsById(id)) {
            logger.warning("Can't find a repo with id: " + id);
            throw new NoEntityFoundException("Can't find a repo with id: " + id);
        }

        Repository repository = jpaRepository.getById(id);
        repository.setRepositoryName(repositoryToUpdate.getRepositoryName());

        if (!repository.getRepositoryName().equals(repositoryToUpdate.getRepositoryName())) {
            checkSameRepositoryNames(repository);
        }

        return jpaRepository.save(repository);
    }

    @Override
    public void delete(Long id) {
        if (!jpaRepository.existsById(id)) {
            logger.warning("Can't find a repo with id: " + id);
            throw new NoEntityFoundException("Can't find a repo with id: " + id);
        }

        jpaRepository.deleteById(id);

    }

    @Override
    @Transactional
    public Repository synchronize(Long id, String tokenAuth) throws NoEntityFoundException {
        if (!jpaRepository.existsById(id)) {
            logger.warning("Can't find a repo with id: " + id);
            throw new NoEntityFoundException("Can't find a repo with id: " + id);
        }
        Repository repository = jpaRepository.getById(id);
        String formattedURL = getFormattedURL(repository.getRepositoryURL());
        repository = pullUpRepositoryFromGitHub(formattedURL, repository, tokenAuth);
        entityManager.refresh(repository);
        return repository;
    }
}
