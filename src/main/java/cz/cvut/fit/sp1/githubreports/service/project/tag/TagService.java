package cz.cvut.fit.sp1.githubreports.service.project.tag;

import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.NoEntityFoundException;
import cz.cvut.fit.sp1.githubreports.dao.project.TagJpaRepository;
import cz.cvut.fit.sp1.githubreports.model.project.Tag;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@AllArgsConstructor
@Service("TagService")
public class TagService implements TagSPI {

    TagJpaRepository repository;

    private void checkValidation(Tag tag) {
        if (tag.getProject() == null)
            throw new EntityStateException();
        if (tag.getProject().getTags().stream()
                .anyMatch(tag1 -> tag1.getTagName().equals(tag.getTagName())))
            throw new EntityStateException();
    }

    @Override
    public Collection<Tag> readAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Tag> readById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Tag create(Tag tag) throws EntityStateException {
        if (tag.getTagId() != null) {
            if (repository.existsById(tag.getTagId()))
                throw new EntityStateException();
        }
        checkValidation(tag);
        System.out.println(tag.getTagId() + " " + tag.getProject().getProjectId());
        return repository.save(tag);
    }

    @Override
    public Tag update(Long id, Tag tag) throws EntityStateException {
        if (id == null || !repository.existsById(id))
            throw new NoEntityFoundException();
        checkValidation(tag);
        return repository.save(tag);
    }

    @Override
    public void delete(Long id) {
        if (repository.existsById(id))
            repository.deleteById(id);
        else throw new NoEntityFoundException();
    }
}
