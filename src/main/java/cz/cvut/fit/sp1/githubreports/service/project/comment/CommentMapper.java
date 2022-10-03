package cz.cvut.fit.sp1.githubreports.service.project.comment;

import cz.cvut.fit.sp1.githubreports.model.project.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.openapi.model.CommentDTO;

@Mapper
public interface CommentMapper {

    @Mapping(source = "author.userId", target = "authorId")
    @Mapping(source = "commit.commitId", target = "commitId")
    CommentDTO fromModel(Comment comment);

    @Mapping(target = "author.userId", source = "authorId")
    @Mapping(target = "commit.commitId", source = "commitId")
    Comment toModel(CommentDTO commentDTO);
}
