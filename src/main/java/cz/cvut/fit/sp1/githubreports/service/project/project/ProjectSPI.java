package cz.cvut.fit.sp1.githubreports.service.project.project;

import cz.cvut.fit.sp1.githubreports.model.project.Project;

import java.util.Collection;
import java.util.Optional;

public interface ProjectSPI {

    Collection<Project> readProjects();

    Optional<Project> readProjectById(Long id);

    void createProject(Project project);

    void updateProject(Long id, Project project);

    void deleteProject(Long id);

}
