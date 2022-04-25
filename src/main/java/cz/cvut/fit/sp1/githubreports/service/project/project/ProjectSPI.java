package cz.cvut.fit.sp1.githubreports.service.project.project;

import cz.cvut.fit.sp1.githubreports.model.project.Project;

import java.util.Collection;
import java.util.Optional;

public interface ProjectSPI {

    Collection<Project> readAll();

    Optional<Project> readById(Long id);

    void create(Project project);

    void update(Long id, Project project);

    void delete(Long id);

}
