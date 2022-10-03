package cz.cvut.fit.sp1.githubreports.api.delegate;


import cz.cvut.fit.sp1.githubreports.service.project.comment.CommentSPI;
import org.openapi.api.CommentsApi;

public class CommentsApiDelegateImpl implements CommentsApi {

    private final CommentSPI commentSPI;

    public CommentsApiDelegateImpl(CommentSPI commentSPI) {
        this.commentSPI = commentSPI;
    }

    //TODO

}
