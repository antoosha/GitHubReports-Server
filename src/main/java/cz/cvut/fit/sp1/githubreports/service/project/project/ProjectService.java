package cz.cvut.fit.sp1.githubreports.service.project.project;

import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.NoEntityFoundException;
import cz.cvut.fit.sp1.githubreports.dao.project.ProjectJpaRepository;
import cz.cvut.fit.sp1.githubreports.dao.user.UserJpaRepository;
import cz.cvut.fit.sp1.githubreports.model.project.Project;
import cz.cvut.fit.sp1.githubreports.model.project.Repository;
import cz.cvut.fit.sp1.githubreports.model.project.Tag;
import cz.cvut.fit.sp1.githubreports.model.statistic.Statistic;
import cz.cvut.fit.sp1.githubreports.service.project.repository.RepositorySPI;
import cz.cvut.fit.sp1.githubreports.service.project.tag.TagSPI;
import cz.cvut.fit.sp1.githubreports.service.statistic.statistic.StatisticSPI;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service("ProjectService")
public class ProjectService implements ProjectSPI {

    private final ProjectJpaRepository projectJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final RepositorySPI repositorySPI;
    private final StatisticSPI statisticSPI;
    private final TagSPI tagSPI;

    private void checkValidation(Project project) {
        if (project.getAuthor() == null)
            throw new EntityStateException();
        if (!userJpaRepository.existsById(project.getAuthor().getUserId()))
            throw new EntityStateException();
    }

    @Override
    public List<Project> readAll() {
        return projectJpaRepository.findAll();
    }

    @Override
    public Project readById(Long id) {
        return projectJpaRepository.findById(id).orElseThrow(NoEntityFoundException::new);
    }

    @Override
    public Project create(Project project) throws EntityStateException {
        if (project.getProjectId() != null) {
            if (projectJpaRepository.existsById(project.getProjectId()))
                throw new EntityStateException();
        }
        checkValidation(project);
        if (project.getAuthor().getProjects()
                .stream().anyMatch(project1 -> project1.getProjectName().equals(project.getProjectName()))) {
            throw new EntityStateException();
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
    public Project update(Long id, Project project) throws EntityStateException {
        if (id == null || !projectJpaRepository.existsById(id))
            throw new EntityStateException();
        checkValidation(project);
        Project project1 = projectJpaRepository.getById(id);
        if (!project.getProjectName().equals(project1.getProjectName())) {
            if (project.getAuthor().getProjects()
                    .stream().anyMatch(project2 -> project2.getProjectName().equals(project.getProjectName()))) {
                throw new EntityStateException();
            }
        }
        return projectJpaRepository.save(project);
    }

    @Override
    public void delete(Long id) {
        if (projectJpaRepository.existsById(id))
            projectJpaRepository.deleteById(id);
        else throw new NoEntityFoundException();
    }
}
