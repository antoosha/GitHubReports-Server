package cz.cvut.fit.sp1.githubreports.service.project.project;

import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.model.project.Project;
import cz.cvut.fit.sp1.githubreports.model.project.Repository;
import cz.cvut.fit.sp1.githubreports.model.project.Tag;
import cz.cvut.fit.sp1.githubreports.model.statistic.Statistic;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProjectSPI {

    Collection<Project> readAll();

    Project readById(Long id);

    Project create(Project project) throws EntityStateException;

    Repository createRepository(Long id, String githubToken, Repository repository);

//    Statistic createStatistic(Long id, String statisticType);

    Tag createTag(Long id, Tag tag);

    Project addUser(Long id, String username);

    Project update(Long id, Project project) throws EntityStateException;

    Project removeUser(Long id, String username);

    void delete(Long id);

}
