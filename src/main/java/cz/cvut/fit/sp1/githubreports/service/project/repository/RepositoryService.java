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
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@AllArgsConstructor
@Service("RepositoryService")
public class RepositoryService implements RepositorySPI {

    RepositoryJpaRepository jpaRepository;
    ProjectJpaRepository projectJpaRepository;
    CommitSPI commitSPI;

    @PersistenceContext
    private final EntityManager entityManager;

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

    private String getFormattedURL(String repositoryURL) {
        String[] tokens = repositoryURL.split("/");
        if (tokens.length != 5) {
            throw new IncorrectRequestException("Repository name must be in format owner/repo");
        }
        return tokens[3] + "/" + tokens[4]; //get owner + / + repo
    }

    private void checkName(Repository repository, String newName) {
        //check user's own name
        if (repository.getRepositoryName().isEmpty()) {
            repository.setRepositoryName(newName);
        }
    }


    private Repository pullUpRepositoryFromGitHub(String formattedURL, Repository repository, String tokenAuth) throws IncorrectRequestException, EntityStateException {
        try {
            GitHub gitHub = GitHub.connectUsingOAuth(tokenAuth);
            GHRepository ghRepository = gitHub.getRepository(formattedURL);
            repository = jpaRepository.save(repository); // for create no commits, for update existed commits
            pullUpCommitsFromGHRepository(repository, ghRepository);// pulling up commits and adding them to this
        } catch (IOException e) {
            throw new IncorrectRequestException(e.getMessage());
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

            // 1) коммит существует и там и там -> оставить, не трогать, сохранить, пропустить
            // 2) добавился новый коммит в гитхаб -> нужно добавить их нам
            // 3) из гитхаба удалились коммиты, а у нас такие ещё существуют -> удалить у нас
            for (Commit oldCommit : oldCommits) {
                if (ghCommits.stream().noneMatch(commit -> commit.getSHA1().equals(oldCommit.getHashHubId()))) {
                    commitSPI.changeIsDeleted(oldCommit, true);
                } else {
                    commitSPI.changeIsDeleted(oldCommit, false);
                }
            }

            for (GHCommit ghCommit : ghCommits) {
                try {
                    //1 2
                    if (oldCommits.stream().noneMatch(commit -> commit.getHashHubId().equals(ghCommit.getSHA1()))) {
                        Commit commit = commitSPI.create(new Commit(null,
                                convertToLocalDate(ghCommit.getCommitDate()),
                                ghCommit.getSHA1(),
                                ghCommit.getCommitShortInfo().getAuthor().getName(),
                                ghCommit.getCommitShortInfo().getMessage(),
                                false,
                                jpaRepository.findRepositoryByRepositoryName(repository.getRepositoryName())
                                        .orElseThrow(EntityStateException::new),
                                new ArrayList<>(),
                                new ArrayList<>()));
                    }

                } catch (IOException e) {
                    throw new EntityStateException(e.getMessage());
                }
            }
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
    @Transactional
    public Repository create(Repository repository, String tokenAuth) throws EntityStateException {
        if (repository.getRepositoryId() != null) {
            if (jpaRepository.existsById(repository.getRepositoryId()))
                throw new EntityStateException();
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
    public Repository update(Long id, Repository repository, String tokenAuth) throws EntityStateException {
        if (id == null || !jpaRepository.existsById(id))
            throw new NoEntityFoundException();
        checkValidation(repository);
        String formattedURL = getFormattedURL(repository.getRepositoryURL());
        System.out.println(repository.getRepositoryURL());
        checkName(repository, formattedURL.split("/")[1]);
        if (!repository.getRepositoryName().equals(jpaRepository.getById(id).getRepositoryName()))
            checkSameRepositoryNames(repository);
        if (!repository.getRepositoryURL().equals(jpaRepository.getById(id).getRepositoryURL())){
            repository = pullUpRepositoryFromGitHub(formattedURL, repository, tokenAuth);
        }
        System.out.println(repository.getRepositoryURL());
        entityManager.getEntityManagerFactory().getCache().evictAll();
        System.out.println(repository.getRepositoryURL());
        return repository;
    }

    @Override
    public void delete(Long id) {
        if (jpaRepository.existsById(id))
            jpaRepository.deleteById(id);
        else throw new EntityNotFoundException();
    }

    @Override
    @Transactional
    public Repository synchronize(Long id, String tokenAuth) throws NoEntityFoundException {
        if (!jpaRepository.existsById(id)) {
            throw new NoEntityFoundException();
        }
        Repository repository = jpaRepository.getById(id);
        String formattedURL = getFormattedURL(repository.getRepositoryURL());
        repository = pullUpRepositoryFromGitHub(formattedURL, repository, tokenAuth);
        entityManager.refresh(repository);
        return repository;
    }
}
