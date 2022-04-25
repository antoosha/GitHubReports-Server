package cz.cvut.fit.sp1.githubreports.service.project.comment;

import cz.cvut.fit.sp1.githubreports.api.dto.project.CommentDTO;
import cz.cvut.fit.sp1.githubreports.model.project.Comment;
import cz.cvut.fit.sp1.githubreports.service.project.commit.CommitService;
import cz.cvut.fit.sp1.githubreports.service.user.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class CommentConverter {

    private final UserService userService;
    private final CommitService commitService;

    public Comment toModel(CommentDTO commentDTO) {
        return new Comment(
                commentDTO.getCommentID(),
                commentDTO.getText(),
                commentDTO.getCreatedDate(),
                userService.readById(commentDTO.getAuthorID()).orElseThrow(RuntimeException::new),
                commitService.readById(commentDTO.getCommitID()).orElseThrow(RuntimeException::new)
        );
    }

    public CommentDTO fromModel(Comment comment) {
        return new CommentDTO(
                comment.getCommentId(),
                comment.getText(),
                comment.getCreatedDate(),
                comment.getAuthor().getUserId(),
                comment.getCommit().getCommitId()
        );
    }

    public List<Comment> toModelsMany(List<CommentDTO> comments) {
        return comments.stream().map(this::toModel).collect(Collectors.toList());
    }

    public List<CommentDTO> fromModelsMany(List<Comment> comments) {
        return comments.stream().map(this::fromModel).collect(Collectors.toList());
    }
}
