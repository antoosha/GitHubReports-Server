package cz.cvut.fit.sp1.githubreports.api.controller.project;

import cz.cvut.fit.sp1.githubreports.api.dto.project.TagDTO;
import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.IncorrectRequestException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.NoEntityFoundException;
import cz.cvut.fit.sp1.githubreports.service.project.tag.TagConverter;
import cz.cvut.fit.sp1.githubreports.service.project.tag.TagSPI;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@AllArgsConstructor
@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagSPI tagSPI;
    private final TagConverter tagConverter;

    @GetMapping()
    public Collection<TagDTO> getAll() {
        return tagConverter.fromModelsMany(tagSPI.readAll());
    }

    @GetMapping("/{id}")
    public TagDTO getOne(@PathVariable Long id) {
        return tagConverter.fromModel(tagSPI.readById(id).orElseThrow(NoEntityFoundException::new));
    }

    @PostMapping()
    public TagDTO create(@RequestBody TagDTO tagDTO) throws EntityStateException {
        return tagConverter.fromModel(tagSPI.create(tagConverter.toModel(tagDTO)));
    }

    @PutMapping("/{id}")
    public TagDTO update(@PathVariable Long id, @RequestBody TagDTO tagDTO) throws IncorrectRequestException, EntityStateException {
        if (!tagDTO.getTagID().equals(id))
            throw new IncorrectRequestException();
        return tagConverter.fromModel(tagSPI.update(tagDTO.getTagID(), tagConverter.toModel(tagDTO)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        if (tagSPI.readById(id).isEmpty())
            throw new NoEntityFoundException();
        tagSPI.delete(id);
    }
}
