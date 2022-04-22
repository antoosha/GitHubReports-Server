package cz.cvut.fit.sp1.githubreports.service.project.commit;

import cz.cvut.fit.sp1.githubreports.api.dto.project.CommitDTO;
import cz.cvut.fit.sp1.githubreports.model.project.Comment;
import cz.cvut.fit.sp1.githubreports.model.project.Commit;
import cz.cvut.fit.sp1.githubreports.model.project.Tag;
import cz.cvut.fit.sp1.githubreports.service.project.comment.CommentService;
import cz.cvut.fit.sp1.githubreports.service.project.tag.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

    @Component
    public class CommitConverter {


        private final TagService tagService;
        private final CommentService commentService;

        @Autowired
        public CommitConverter( TagService tagService, CommentService commentService) {
            this.tagService = tagService;
            this.commentService = commentService;
        }

        public Commit toModel(CommitDTO commitDTO) {
            return new Commit(
                    commitDTO.getCommitID(),
                    commitDTO.getCreatedDate(),
                    commitDTO.getHashHubID(),
                    commitDTO.getLoginAuthor(),
                    commitDTO.getDescription(),
                    commitDTO.getRepositoryID(),
                    commitDTO.getTagsIDs().stream().map(e -> tagService.getById(e)).collect(Collectors.toList()),
                    commitDTO.getCommentsIDs().stream().map(e -> commentService.getById(e)).collect(Collectors.toList())
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

        public Collection<Commit> toModelMany(Collection<CommitDTO> commitDTOS) {
            return commitDTOS.stream().map(this::toModel).collect(Collectors.toList());
        }

        public Collection<CommitDTO> fromModelMany(Collection<Commit> commits) {
            return commits.stream().map(this::fromModel).collect(Collectors.toList());
        }
    }
