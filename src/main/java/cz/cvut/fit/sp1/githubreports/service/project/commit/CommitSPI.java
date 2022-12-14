package cz.cvut.fit.sp1.githubreports.service.project.commit;
import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.model.project.Comment;
import cz.cvut.fit.sp1.githubreports.model.project.Commit;
import org.openapi.model.CommentDTO;
import org.openapi.model.CommentUpdateSlimDTO;

import java.util.Collection;
import java.util.List;

public interface CommitSPI {

    Collection<Commit> readAll();

    Commit readById(Long id);

    List<CommentDTO> readAllCommentsById(Long id, Integer page, Integer size);

    Commit create(Commit commit) throws EntityStateException;

    Commit update(Long id, Commit commit) throws EntityStateException;

    void delete(Long id);

    void changeIsDeleted(Commit commit, boolean isDeleted);

    Comment addComment(Long id, CommentUpdateSlimDTO commentUpdateSlimDTO);

    Commit addTag(Long id, Long tagId);

    Commit deleteComment(Long id, Long commentId);

    Commit removeTag(Long id, Long tagId);
}
