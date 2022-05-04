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

    @GetMapping()
    public Collection<RepositoryDTO> getAll() {
        return repositoryConverter.fromModelsMany(repositorySPI.readAll());
    }
    @GetMapping("/{id}")
    public RepositoryDTO getOne(@PathVariable Long id) {
        return repositoryConverter.fromModel(repositorySPI.readById(id).orElseThrow(NoEntityFoundException::new));
    }

    @PostMapping()
    public RepositoryDTO create(@RequestBody RepositoryDTO repositoryDTO) throws EntityStateException {
        return repositoryConverter.fromModel(repositorySPI.create(repositoryConverter.toModel(repositoryDTO)));
    }

    @PutMapping("/{id}")
    public RepositoryDTO update(@PathVariable Long id, @RequestBody RepositoryDTO repositoryDTO) throws IncorrectRequestException, EntityStateException {
        if (!repositoryDTO.getRepositoryID().equals(id))
            throw new IncorrectRequestException();
        return repositoryConverter.fromModel(repositorySPI.update(repositoryDTO.getRepositoryID(), repositoryConverter.toModel(repositoryDTO)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        if (repositorySPI.readById(id).isEmpty())
            throw new NoEntityFoundException();
        repositorySPI.delete(id);
    }
}
