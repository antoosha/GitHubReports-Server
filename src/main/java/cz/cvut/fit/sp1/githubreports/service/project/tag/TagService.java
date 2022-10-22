package cz.cvut.fit.sp1.githubreports.service.project.tag;

import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.NoEntityFoundException;
import cz.cvut.fit.sp1.githubreports.dao.project.TagJpaRepository;
import cz.cvut.fit.sp1.githubreports.model.project.Tag;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.logging.Logger;

@AllArgsConstructor
@Service("TagService")
public class TagService implements TagSPI {

    TagJpaRepository repository;

    private static final Logger logger = Logger.getLogger(TagService.class.getName());

    private void checkValidation(Tag tag) {
        if (tag.getProject() == null)
            throw new EntityStateException("Tag with id " + tag.getTagId() + " does not have project.");
        if (tag.getTagId() != null) {
            if (!tag.getTagName().equals(this.readById(tag.getTagId()).getTagName()))
                if (tag.getProject().getTags().stream()
                        .anyMatch(tag1 -> tag1.getTagName().equals(tag.getTagName()))){
                    logger.warning("Project with id " + tag.getProject().getProjectId()
                            + " already has tag with name " + tag.getTagName());
                    throw new EntityStateException
                            ("Project with id " + tag.getProject().getProjectId()
                                    + " already has tag with name " + tag.getTagName());
                }

        } else {
            if (tag.getProject().getTags().stream()
                    .anyMatch(tag1 -> tag1.getTagName().equals(tag.getTagName()))){
                logger.warning("Project with id " + tag.getProject().getProjectId()
                        + " already has tag with name " + tag.getTagName());
                throw new EntityStateException
                        ("Project with id " + tag.getProject().getProjectId()
                                + " already has tag with name " + tag.getTagName());
            }

        }
    }

    @Override
    public Collection<Tag> readAll() {
        return repository.findAll();
    }

    @Override
    public Tag readById(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> {
                    logger.warning("Tag with id " + id + " does not exist.");
                    return new NoEntityFoundException("Tag with id " + id + " does not exist.");
                });
    }

    @Override
    public Tag create(Tag tag) throws EntityStateException {
        if (tag.getTagId() != null && repository.existsById(tag.getTagId())) {
            logger.warning("Tag with id " + tag.getTagId() + " does not exist.");
            throw new EntityStateException("Tag with id " + tag.getTagId() + " does not exist.");
        }
        checkValidation(tag);
        return repository.save(tag);
    }

    @Override
    public Tag update(Long id, Tag tag) throws EntityStateException {
        if (id == null || !repository.existsById(id)){
            logger.warning("Tag with id " + id + " does not exist.");
            throw new NoEntityFoundException("Tag with id " + id + " does not exist.");
        }
        checkValidation(tag);
        return repository.save(tag);
    }

    @Override
    public void delete(Long id) {
        if (repository.existsById(id))
            repository.deleteById(id);
        else {
            logger.warning("Tag with id " + id + " does not exist.");
            throw new NoEntityFoundException("Tag with id " + id + " does not exist.");
        }
    }
}
