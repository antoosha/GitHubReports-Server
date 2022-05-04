package cz.cvut.fit.sp1.githubreports.service.project.project;

import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.model.project.Project;

import java.util.Collection;
import java.util.Optional;

public interface ProjectSPI {

    Collection<Project> readAll();

    Optional<Project> readById(Long id);

    Project create(Project project) throws EntityStateException;

    Project update(Long id, Project project) throws EntityStateException;

    void delete(Long id);

}
