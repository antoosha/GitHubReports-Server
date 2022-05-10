package cz.cvut.fit.sp1.githubreports.api.controller.project;


import cz.cvut.fit.sp1.githubreports.api.dto.project.CommitDTO;
import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.IncorrectRequestException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.NoEntityFoundException;
import cz.cvut.fit.sp1.githubreports.service.project.commit.CommitConverter;
import cz.cvut.fit.sp1.githubreports.service.project.commit.CommitSPI;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@AllArgsConstructor
@RestController
@RequestMapping("/commits")
public class CommitController {
    private final CommitSPI commitSPI;
    private final CommitConverter commitConverter;

    @GetMapping()
    public Collection<CommitDTO> getAll() {
        return commitConverter.fromModelsMany(commitSPI.readAll());
    }

    @GetMapping("/{id}")
    public CommitDTO getOne(@PathVariable Long id) {
        return commitConverter.fromModel(commitSPI.readById(id).orElseThrow(NoEntityFoundException::new));
    }

    @PostMapping()
    public CommitDTO create(@RequestBody CommitDTO commitDTO) throws EntityStateException {
        return commitConverter.fromModel(commitSPI.create(commitConverter.toModel(commitDTO)));
    }

    @PutMapping("/{id}")
    public CommitDTO update(@PathVariable Long id, @RequestBody CommitDTO commitDTO) throws IncorrectRequestException, EntityStateException {
        if (!commitDTO.getCommitID().equals(id))
            throw new IncorrectRequestException();
        return commitConverter.fromModel(commitSPI.update(commitDTO.getCommitID(), commitConverter.toModel(commitDTO)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        if (commitSPI.readById(id).isEmpty())
            throw new NoEntityFoundException();
        commitSPI.delete(id);
    }

}
