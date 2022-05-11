package cz.cvut.fit.sp1.githubreports.api.controller.project;

import cz.cvut.fit.sp1.githubreports.api.dto.project.RepositoryDTO;
import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.IncorrectRequestException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.NoEntityFoundException;
import cz.cvut.fit.sp1.githubreports.service.project.repository.RepositoryConverter;
import cz.cvut.fit.sp1.githubreports.service.project.repository.RepositorySPI;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@AllArgsConstructor
@RestController
@RequestMapping("/repositories")
public class RepositoryController {

    private final RepositorySPI repositorySPI;
    private final RepositoryConverter repositoryConverter;

    /**
     * GET: "/repositories"
     *
     * @return collection of all repositories in our database.
     */
    @GetMapping()
    public Collection<RepositoryDTO> getAll() {
        return repositoryConverter.fromModelsMany(repositorySPI.readAll());
    }

    /**
     * GET: "/repositories/{id}"
     *
     * @return repository by id.
     */
    @GetMapping("/{id}")
    public RepositoryDTO getOne(@PathVariable Long id) {
        return repositoryConverter.fromModel(repositorySPI.readById(id).orElseThrow(NoEntityFoundException::new));
    }

    /**
     * Create new repository.
     * <p>
     * POST: "/repositories"
     * <p>
     * Request body example:
     * {
     * "repositoryName": "repositoryName",
     * "repositoryURL": "https://github.com/user/repositoryName",
     * "projectID": [
     * 1
     * ],
     * "commitsIDs": []
     * }
     * projectID - cannot be empty.
     *
     * @return created repository.
     */
    @PostMapping("/{tokenAuth}")
    public RepositoryDTO create(@RequestBody RepositoryDTO repositoryDTO, @PathVariable("tokenAuth") String tokenAuth) throws EntityStateException {
        return repositoryConverter.fromModel(repositorySPI.create(repositoryConverter.toModel(repositoryDTO), tokenAuth));
    }

    /**
     * Update repository by id.
     * <p>
     * PUT: "/repositories/{id}"
     * <p>
     * Request body example:
     * {
     * "repositoryID": 1,
     * "repositoryName": "repository",
     * "repositoryURL": "https://github.com/user/repositoryName",
     * "commitsIDs": [],
     * "projectsIDs": [
     * 1
     * ]
     * }
     *
     * @return updated repository.
     */
    @PutMapping("/{id}")
    public RepositoryDTO update(@PathVariable Long id, @RequestBody RepositoryDTO repositoryDTO) throws IncorrectRequestException, EntityStateException {
        if (!repositoryDTO.getRepositoryID().equals(id))
            throw new IncorrectRequestException();
        return repositoryConverter.fromModel(repositorySPI.update(repositoryDTO.getRepositoryID(), repositoryConverter.toModel(repositoryDTO)));
    }

    /**
     * Delete repository by id.
     * DELETE: "/repositories/{id}"
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        if (repositorySPI.readById(id).isEmpty())
            throw new NoEntityFoundException();
        repositorySPI.delete(id);
    }
}
