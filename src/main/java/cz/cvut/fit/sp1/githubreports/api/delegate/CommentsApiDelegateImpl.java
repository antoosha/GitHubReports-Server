package cz.cvut.fit.sp1.githubreports.api.delegate;

import cz.cvut.fit.sp1.githubreports.api.exceptions.NoEntityFoundException;
import cz.cvut.fit.sp1.githubreports.service.project.comment.CommentMapper;
import cz.cvut.fit.sp1.githubreports.service.project.comment.CommentSPI;
import lombok.RequiredArgsConstructor;
import org.openapi.api.CommentsApi;
import org.openapi.model.CommentDTO;
import org.openapi.model.CommentSlimDTO;
import org.openapi.model.CommentUpdateSlimDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class CommentsApiDelegateImpl implements CommentsApi {

    private final CommentSPI commentSPI;

    private final CommentMapper commentMapper;

    @Override
    public ResponseEntity<CommentDTO> getComment(Long id) {
        return ResponseEntity.ok(
            commentMapper.toDTO(commentSPI.readById(id)));
    }

    @Override
    public ResponseEntity<CommentSlimDTO> updateComment(Long id,
        CommentUpdateSlimDTO commentUpdateSlimDTO) {
        return ResponseEntity.ok(commentMapper.toSlimDTO(
            commentSPI.update(id, commentMapper.fromUpdateSlimDTO(commentUpdateSlimDTO))));
    }
}
