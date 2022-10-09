package cz.cvut.fit.sp1.githubreports.service.project.tag;

import cz.cvut.fit.sp1.githubreports.model.project.Tag;
import cz.cvut.fit.sp1.githubreports.model.project.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.openapi.model.TagDTO;
import org.openapi.model.TagSlimDTO;
import org.openapi.model.TagDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagMapper {

    TagDTO toDTO(Tag tag);

    TagSlimDTO toSlimDTO(Tag tag);

    Tag fromSlimDTO(TagSlimDTO tagSlimDTO);

    List<TagDTO> toDTOs(List<Tag> tags);

    List<TagSlimDTO> toSlimDTOs(List<Tag> tags);
    
}
