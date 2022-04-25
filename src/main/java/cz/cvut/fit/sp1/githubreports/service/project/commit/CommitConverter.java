package cz.cvut.fit.sp1.githubreports.service.project.commit;

import cz.cvut.fit.sp1.githubreports.api.dto.project.CommitDTO;
import cz.cvut.fit.sp1.githubreports.model.project.Comment;
import cz.cvut.fit.sp1.githubreports.model.project.Commit;
import cz.cvut.fit.sp1.githubreports.model.project.Tag;
import cz.cvut.fit.sp1.githubreports.service.project.comment.CommentService;
import cz.cvut.fit.sp1.githubreports.service.project.repository.RepositoryService;
import cz.cvut.fit.sp1.githubreports.service.project.tag.TagService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Converter;
import java.util.Collection;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class CommitConverter {

    private final TagService tagService;
    private final CommentService commentService;
    private final RepositoryService repositoryService;


    public Commit toModel(CommitDTO commitDTO) {
        return new Commit(
                commitDTO.getCommitID(),
                commitDTO.getCreatedDate(),
                commitDTO.getHashHubID(),
                commitDTO.getLoginAuthor(),
                commitDTO.getDescription(),
                repositoryService.readById(commitDTO.getRepositoryID()).orElseThrow(RuntimeException::new),
                commitDTO.getTagsIDs().stream().map(tagID -> tagService.readById(tagID).orElseThrow(RuntimeException::new)).collect(Collectors.toList()),
                commitDTO.getCommentsIDs().stream().map(commentID -> commentService.readById(commentID).orElseThrow(RuntimeException::new)).collect(Collectors.toList())
        );
    }

    public CommitDTO fromModel(Commit commit) {
        return new CommitDTO(
                commit.getCommitId(),
                commit.getCreatedDate(),
                commit.getHashHubId(),
                commit.getLoginAuthor(),
                commit.getDescription(),
                commit.getRepository().getRepositoryId(),
                commit.getTags().stream().map(Tag::getTagId).collect(Collectors.toList()),
                commit.getComments().stream().map(Comment::getCommentId).collect(Collectors.toList())
        );
    }

    public Collection<Commit> toModelsMany(Collection<CommitDTO> commitDTOS) {
        return commitDTOS.stream().map(this::toModel).collect(Collectors.toList());
    }

    public Collection<CommitDTO> fromModelsMany(Collection<Commit> commits) {
        return commits.stream().map(this::fromModel).collect(Collectors.toList());
    }
}
