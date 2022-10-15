package cz.cvut.fit.sp1.githubreports.service.project.commit;
import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.model.project.Commit;
import org.openapi.model.CommentUpdateSlimDTO;

import java.util.Collection;
import java.util.Optional;

public interface CommitSPI {

    Collection<Commit> readAll();

    Optional<Commit> readById(Long id);

    Commit create(Commit commit) throws EntityStateException;

    Commit update(Long id, Commit commit) throws EntityStateException;

    void delete(Long id);

    void changeIsDeleted(Commit commit, boolean isDeleted);

    Commit addComment(Long id, CommentUpdateSlimDTO commentUpdateSlimDTO);

    Commit addTag(Long id, Long tagId);

    Commit deleteComment(Long id, Long commentId);

    Commit removeTag(Long id, Long tagId);
}
