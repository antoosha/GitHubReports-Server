package cz.cvut.fit.sp1.githubreports.service.project.comment;


import cz.cvut.fit.sp1.githubreports.api.dto.project.CommentDTO;
import cz.cvut.fit.sp1.githubreports.model.project.Comment;
import cz.cvut.fit.sp1.githubreports.service.project.commit.CommitService;
import cz.cvut.fit.sp1.githubreports.service.user.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentConverter {

    private final UserService user;
    private final CommitService commitService;

    @Autowired
    public CommentConverter(UserService user, CommitService commitService) {
        this.user = user;
        this.commitService = commitService;
    }

    public Comment toModel(CommentDTO commentDTO){
        return new Comment(
                commentDTO.getCommentID(),
                commentDTO.getText(),
                commentDTO.getCreatedDate(),
                user.getById(commentDTO.getAuthorID()),
                commitService.getById(commentDTO.getCommitID())
        );
    }

    public CommentDTO fromModel(Comment comment){
        return new CommentDTO(
                comment.getCommentId(),
                comment.getText(),
                comment.getCreatedDate(),
                user.getId(comment.getAuthor()),
                commitService.getId(comment.getCommit())
        );
    }

    public List<Comment> toModelMany(List<CommentDTO> comments){
        return comments.stream().map(this::toModel).collect(Collectors.toList());
    }

    public List<CommentDTO> fromModelMany(List<Comment> comments){
        return comments.stream().map(this::fromModel).collect(Collectors.toList());
    }
}
