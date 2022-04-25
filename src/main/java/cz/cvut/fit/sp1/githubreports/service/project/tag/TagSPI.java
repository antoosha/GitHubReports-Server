package cz.cvut.fit.sp1.githubreports.service.project.tag;

import cz.cvut.fit.sp1.githubreports.model.project.Tag;

import java.util.Collection;
import java.util.Optional;

public interface TagSPI {

    Collection<Tag> readAll();

    Optional<Tag> readById(Long id);

    void create(Tag tag);

    void update(Long id, Tag tag);

    void delete(Long id);
}

