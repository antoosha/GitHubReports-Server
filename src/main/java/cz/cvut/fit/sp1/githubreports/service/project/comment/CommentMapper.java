package cz.cvut.fit.sp1.githubreports.service.project.comment;

import cz.cvut.fit.sp1.githubreports.model.project.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.openapi.model.CommentDTO;
import org.openapi.model.CommentSlimDTO;
import org.openapi.model.CommentUpdateSlimDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentDTO toDTO(Comment comment);

    CommentSlimDTO toSlimDTO(Comment comment);

    @Mapping(target = "author", ignore = true)
    @Mapping(target = "authorUsername", ignore = true)
    @Mapping(target = "commit", ignore = true)
    Comment fromSlimDTO(CommentSlimDTO commentSlimDTO);

    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "authorUsername", ignore = true)
    @Mapping(target = "commit", ignore = true)
    @Mapping(target = "isEdited", ignore = true)
    Comment fromUpdateSlimDTO(CommentUpdateSlimDTO commentUpdateSlimDTO);

    List<CommentDTO> toDTOs(List<Comment> comments);

    List<CommentSlimDTO> toSlimDTOs(List<Comment> comments);

}
