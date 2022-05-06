package cz.cvut.fit.sp1.githubreports.service.project.comment;

import cz.cvut.fit.sp1.githubreports.api.dto.project.CommentDTO;
import cz.cvut.fit.sp1.githubreports.api.exceptions.IncorrectRequestException;
import cz.cvut.fit.sp1.githubreports.model.project.Comment;
import cz.cvut.fit.sp1.githubreports.service.project.commit.CommitSPI;
import cz.cvut.fit.sp1.githubreports.service.user.user.UserSPI;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class CommentConverter {

    private final UserSPI userSPI;
    private final CommitSPI commitSPI;

    public Comment toModel(CommentDTO commentDTO) {
        return new Comment(
                commentDTO.getCommentID(),
                commentDTO.getText(),
                commentDTO.getCreatedDate(),
                userSPI.readById(commentDTO.getAuthorID()).orElseThrow(IncorrectRequestException::new),
                commitSPI.readById(commentDTO.getCommitID()).orElseThrow(IncorrectRequestException::new)
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

    public Collection<Comment> toModelsMany(List<CommentDTO> comments) {
        return comments.stream().map(this::toModel).collect(Collectors.toList());
    }

    public Collection<CommentDTO> fromModelsMany(List<Comment> comments) {
        return comments.stream().map(this::fromModel).collect(Collectors.toList());
    }
}
