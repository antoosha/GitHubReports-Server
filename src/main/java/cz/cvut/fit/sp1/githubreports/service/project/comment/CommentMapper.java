package cz.cvut.fit.sp1.githubreports.service.project.comment;

import cz.cvut.fit.sp1.githubreports.model.project.Comment;
import org.mapstruct.Mapper;
import org.openapi.model.CommentDTO;
import org.openapi.model.CommentSlimDTO;

import java.util.List;

@Mapper
public interface CommentMapper {

    CommentDTO toDTO(Comment comment);

    List<CommentDTO> toDTOsMany(List<Comment> comments);

    CommentSlimDTO toSlimDTO(Comment comment);

    List<CommentSlimDTO> toSlimDTOsMany(List<Comment> comments);

    Comment fromDTO(CommentDTO commentDTO);

    Comment fromSlimDTO(CommentSlimDTO commentSlimDTO);
}
