package cz.cvut.fit.sp1.githubreports.api.controller.project;

import cz.cvut.fit.sp1.githubreports.api.dto.project.ProjectDTO;
import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.IncorrectRequestException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.NoEntityFoundException;
import cz.cvut.fit.sp1.githubreports.service.project.project.ProjectConverter;
import cz.cvut.fit.sp1.githubreports.service.project.project.ProjectSPI;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectSPI projectSPI;
    private final ProjectConverter projectConverter;

    public ProjectController(@Qualifier("ProjectService") ProjectSPI projectSPI, ProjectConverter projectConverter) {
        this.projectSPI = projectSPI;
        this.projectConverter = projectConverter;
    }

    /**
         GET: "/projects"
         @return collection of all projects in our database.
     */
    @GetMapping()
    public Collection<ProjectDTO> getAll() {
        return projectConverter.fromModelsMany(projectSPI.readAll());
    }

    /**
         GET: "/projects/{id}"
         @return project by id.
     */
    @GetMapping("/{id}")
    public ProjectDTO getOne(@PathVariable Long id) {
        return projectConverter.fromModel(projectSPI.readById(id).orElseThrow(NoEntityFoundException::new));
    }

    /**
         Create new project.

         POST: "/projects"

         Request body example:
         {
             "projectName": "project name",
             "description": "desc",
             "authorID": 2,
             "repositoriesIDs": [],
             "statisticsIDs": [],
             "usersIDs": [2],
             "tagsIDs": []
         }
         @return created project.
     */
    @PostMapping()
    public ProjectDTO create(@RequestBody ProjectDTO projectDTO) throws EntityStateException {
        return projectConverter.fromModel(projectSPI.create(projectConverter.toModel(projectDTO)));
    }

    /**
         Update project by id.
         PUT: "/projects/{id}"

         Request body example:
         {
             "projectID": 1,
             "createdDate": "2022-05-05T22:16:42.304572",
             "projectName": "project3",
             "description": "desc",
             "authorID": 2,
             "repositoriesIDs": [],
             "statisticsIDs": [],
             "usersIDs": [2],
             "tagsIDs": []
         }
         @return updated project.
     */
    @PutMapping("/{id}")
    public ProjectDTO update(@PathVariable Long id, @RequestBody ProjectDTO projectDTO) throws IncorrectRequestException, EntityStateException {
        if (!projectDTO.getProjectID().equals(id))
            throw new IncorrectRequestException();
        return projectConverter.fromModel(projectSPI.update(projectDTO.getProjectID(), projectConverter.toModel(projectDTO)));
    }

    /**
         Delete project by id.
         DELETE: "/projects/{id}"
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        if (projectSPI.readById(id).isEmpty())
            throw new NoEntityFoundException();
        projectSPI.delete(id);
    }
}
