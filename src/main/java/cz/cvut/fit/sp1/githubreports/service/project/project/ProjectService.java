package cz.cvut.fit.sp1.githubreports.service.project.project;

import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.dao.project.ProjectJpaRepository;
import cz.cvut.fit.sp1.githubreports.model.project.Project;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@AllArgsConstructor
@Service("ProjectService")
public class ProjectService implements ProjectSPI {

    private final ProjectJpaRepository repository;

    @Override
    public Collection<Project> readAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Project> readById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Project create(Project project) throws EntityStateException {
        if (repository.existsById(project.getProjectId()))
            throw new EntityStateException();
        return repository.save(project);
    }

    @Override
    public Project update(Long id, Project project) throws EntityStateException {
        if (!repository.existsById(id))
            throw new EntityStateException();
        return repository.save(project);
    }

    @Override
    public void delete(Long id) {
        if (repository.existsById(id))
            repository.deleteById(id);
    }
}
