package cz.cvut.fit.sp1.githubreports.service.project.project;

import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.NoEntityFoundException;
import cz.cvut.fit.sp1.githubreports.dao.project.ProjectJpaRepository;
import cz.cvut.fit.sp1.githubreports.dao.user.UserJpaRepository;
import cz.cvut.fit.sp1.githubreports.model.project.Project;
import cz.cvut.fit.sp1.githubreports.model.project.Repository;
import cz.cvut.fit.sp1.githubreports.model.project.Tag;
import cz.cvut.fit.sp1.githubreports.model.statistic.Statistic;
import cz.cvut.fit.sp1.githubreports.model.user.User;
import cz.cvut.fit.sp1.githubreports.service.project.repository.RepositorySPI;
import cz.cvut.fit.sp1.githubreports.service.project.tag.TagSPI;
import cz.cvut.fit.sp1.githubreports.service.statistic.statistic.StatisticSPI;
import cz.cvut.fit.sp1.githubreports.service.user.user.UserSPI;
import java.util.logging.Logger;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service("ProjectService")
public class ProjectService implements ProjectSPI {

    private static final Logger logger = Logger.getLogger(ProjectService.class.getName());
    
    private final ProjectJpaRepository projectJpaRepository;

    private final UserSPI userSPI;
    private final RepositorySPI repositorySPI;
    private final TagSPI tagSPI;

    @Override
    public Collection<Project> readAll() {
        return projectJpaRepository.findAll();
    }

    @Override
    public Project readById(Long id) {
        return projectJpaRepository.findById(id)
            .orElseThrow(() -> {
                logger.warning("Can't find a project with id: " + id);
                return new NoEntityFoundException("Can't find a project with id: " + id);
            });
    }

    @Override
    public Project create(Project project) throws EntityStateException {
        User user = userSPI.readUserFromToken();
        project.setAuthor(user);
        project.getUsers().add(user);

        if (project.getAuthor().getProjects().stream()
            .anyMatch(project1 -> project1.getProjectName().equals(project.getProjectName()))) {
            logger.warning("User already has a project with such name: " + project);
            throw new EntityStateException("User already has a project with such name: " + project);
        }

        project.setCreatedDate(LocalDateTime.now());
        return projectJpaRepository.save(project);
    }

    @Override
    public Repository createRepository(Long id, String githubToken, Repository repository) {
        repository.setProject(readById(id));
        return repositorySPI.create(repository, githubToken);
    }

//    @Override
//    public Statistic createStatistic(Long id, String statisticType) {
//        //TODO implement logic;
//    }

    @Override
    public Tag createTag(Long id, Tag tag) {
        tag.setProject(readById(id));
        tag.setCommits(new ArrayList<>());
        return tagSPI.create(tag);
    }

    @Override
    public Project addUser(Long id, String username) {
        Project project = readById(id);
        User userToAdd = userSPI.readByUsername(username);
        project.getUsers().add(userToAdd);
        return projectJpaRepository.save(project);
    }

    @Override
    public Project update(Long id, Project projectToUpdate) throws EntityStateException {
        if (id == null || !projectJpaRepository.existsById(id))
            throw new EntityStateException("Can't find a project with id: " + id);
        Project projectFromDB = projectJpaRepository.getById(id);
        if (!projectToUpdate.getProjectName().equals(projectFromDB.getProjectName())) {
            if (projectFromDB.getAuthor().getProjects().stream().anyMatch(
                project -> project.getProjectName().equals(projectToUpdate.getProjectName()))) {
                logger.warning("User already has a project with such name: " + projectToUpdate);
                throw new EntityStateException(
                    "User already has a project with such name: " + projectToUpdate);
            }
        }
        projectFromDB.setDescription(projectToUpdate.getDescription());
        projectFromDB.setProjectName(projectToUpdate.getProjectName());
        return projectJpaRepository.save(projectFromDB);
    }

    @Override
    public Project removeUser(Long id, String username) {
        Project project = readById(id);
        User userToAdd = userSPI.readByUsername(username);
        project.getUsers().remove(userToAdd);
        return projectJpaRepository.save(project);
    }

    @Override
    public void delete(Long id) {
        if (!projectJpaRepository.existsById(id)) {
            logger.warning("Can't find a project with id: " + id);
            throw new NoEntityFoundException("Can't find a project with id: " + id);
        }

        projectJpaRepository.deleteById(id);
    }
}
