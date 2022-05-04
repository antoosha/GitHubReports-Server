package cz.cvut.fit.sp1.githubreports.service.project.tag;

import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.model.project.Tag;

import java.util.Collection;
import java.util.Optional;

public interface TagSPI {

    Collection<Tag> readAll();

    Optional<Tag> readById(Long id);

    Tag create(Tag tag) throws EntityStateException;

    Tag update(Long id, Tag tag) throws EntityStateException;

    void delete(Long id);
}

