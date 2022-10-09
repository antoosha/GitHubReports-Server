package cz.cvut.fit.sp1.githubreports.api.delegate;


import cz.cvut.fit.sp1.githubreports.api.exceptions.NoEntityFoundException;
import cz.cvut.fit.sp1.githubreports.model.project.Comment;
import cz.cvut.fit.sp1.githubreports.service.project.comment.CommentMapper;
import cz.cvut.fit.sp1.githubreports.service.project.comment.CommentSPI;
import org.openapi.api.CommentsApi;
import org.openapi.model.CommentDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CommentsApiDelegateImpl implements CommentsApi {

    private final CommentSPI commentSPI;

    private final CommentMapper commentMapper;

    public CommentsApiDelegateImpl(CommentSPI commentSPI,
                                   CommentMapper commentMapper) {
        this.commentSPI = commentSPI;
        this.commentMapper = commentMapper;
    }

//    @Override
//    public ResponseEntity<CommentDTO> getComment(Long id) {
//        Comment comment = commentSPI.readById(id).orElseThrow(() -> new NoEntityFoundException());
//        CommentDTO commentDTO = commentMapper.fromModel(comment);
//        return new ResponseEntity<>(commentDTO, HttpStatus.OK);
//    }
}
