package cz.cvut.fit.sp1.githubreports.api.controller.project;

import cz.cvut.fit.sp1.githubreports.api.dto.project.ProjectDTO;
import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.IncorrectRequestException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.NoEntityFoundException;
import cz.cvut.fit.sp1.githubreports.service.project.project.ProjectConverter;
import cz.cvut.fit.sp1.githubreports.service.project.project.ProjectSPI;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@AllArgsConstructor
@RestController
@RequestMapping("/repositories")
public class ProjectController {

    private final ProjectSPI projectSPI;
    private final ProjectConverter projectConverter;

    @GetMapping()
    public Collection<ProjectDTO> getAll() {
        return projectConverter.fromModelsMany(projectSPI.readAll());
    }
    
    @GetMapping("/{id}")
    public ProjectDTO getOne(@PathVariable Long id) {
        return projectConverter.fromModel(projectSPI.readById(id).orElseThrow(NoEntityFoundException::new));
    }

    @PostMapping()
    public ProjectDTO create(@RequestBody ProjectDTO projectDTO) throws EntityStateException {
        return projectConverter.fromModel(projectSPI.create(projectConverter.toModel(projectDTO)));
    }

    @PutMapping("/{id}")
    public ProjectDTO update(@PathVariable Long id, @RequestBody ProjectDTO projectDTO) throws IncorrectRequestException, EntityStateException {
        if (!projectDTO.getProjectID().equals(id))
            throw new IncorrectRequestException();
        return projectConverter.fromModel(projectSPI.update(projectDTO.getProjectID(), projectConverter.toModel(projectDTO)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        if (projectSPI.readById(id).isEmpty())
            throw new NoEntityFoundException();
        projectSPI.delete(id);
    }
}
