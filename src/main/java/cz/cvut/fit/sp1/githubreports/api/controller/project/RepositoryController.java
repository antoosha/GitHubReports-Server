package cz.cvut.fit.sp1.githubreports.api.controller.project;

import cz.cvut.fit.sp1.githubreports.api.dto.project.RepositoryDTO;
import cz.cvut.fit.sp1.githubreports.api.dto.project.RepositoryUpdateDTO;
import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.IncorrectRequestException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.NoEntityFoundException;
import cz.cvut.fit.sp1.githubreports.service.project.repository.RepositoryConverter;
import cz.cvut.fit.sp1.githubreports.service.project.repository.RepositorySPI;
import cz.cvut.fit.sp1.githubreports.service.project.repository.RepositoryUpdateConverter;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@AllArgsConstructor
@RestController
@RequestMapping("/repositories")
public class RepositoryController {

    private final RepositorySPI repositorySPI;
    private final RepositoryConverter repositoryConverter;
    private final RepositoryUpdateConverter repositoryUpdateConverter;

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
     * POST: "/repositories/{tokenAuth}" where tokenAuth is oauth token from github
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
     * @param tokenAuth
     * @return created repository.
     */
    @PostMapping("/{tokenAuth}")
    public RepositoryDTO create(@RequestBody RepositoryDTO repositoryDTO, @PathVariable("tokenAuth") String tokenAuth) throws EntityStateException {
        return repositoryConverter.fromModel(repositorySPI.create(repositoryConverter.toModel(repositoryDTO), tokenAuth));
    }

    /**
     * Update repository by id.
     * <p>
     * PUT: "/repositories/{id}/{tokenAuth}" where tokenAuth is oauth token from github
     * <p>
     * Request body example:
     * {
     * "repositoryID": 1,
     * "repositoryName": "repository",
     * "repositoryURL": "https://github.com/user/repositoryName",
     * "projectsIDs": [
     * 1
     * ]
     * }
     *
     * @param id
     * @param tokenAuth
     * @return updated repository.
     */
    @PutMapping("/{id}/{tokenAuth}")
    public RepositoryDTO update(@PathVariable Long id, @RequestBody RepositoryUpdateDTO repositoryUpdateDTO, @PathVariable("tokenAuth") String tokenAuth) throws IncorrectRequestException, EntityStateException {
        if (!repositoryUpdateDTO.getRepositoryID().equals(id))
            throw new IncorrectRequestException();
        return repositoryConverter.fromModel(repositorySPI.update(repositoryUpdateDTO.getRepositoryID(), repositoryUpdateConverter.toModel(repositoryUpdateDTO), tokenAuth));
    }

    /**
     * Delete repository by id.
     * DELETE: "/repositories/{id}"
     * @param id
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        if (repositorySPI.readById(id).isEmpty())
            throw new NoEntityFoundException();
        repositorySPI.delete(id);
    }

    /**
     * Synchronize existing repository by id.
     * @param id
     * @param tokenAuth
     */
    @PutMapping("/synchronize/{id}/{tokenAuth}")
    public RepositoryDTO synchronize(@PathVariable("id") Long id, @PathVariable("tokenAuth") String tokenAuth) {
        return repositoryConverter.fromModel(repositorySPI.synchronize(id, tokenAuth));
    }
}
