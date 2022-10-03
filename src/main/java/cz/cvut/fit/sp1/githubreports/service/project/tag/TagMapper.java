package cz.cvut.fit.sp1.githubreports.service.project.tag;

import cz.cvut.fit.sp1.githubreports.model.project.Tag;
import org.mapstruct.Mapping;
import org.openapi.model.TagDTO;

public interface TagMapper {

    @Mapping(source = "project.projectId", target = "projectId")
    @Mapping(source = "commits", target = "commitsIds")
    TagDTO fromModel(Tag tag);

    @Mapping(target = "project.projectId", source = "projectId")
    @Mapping(target = "commits", source = "commitsIds")
    Tag toModel(TagDTO tagDTO);
}
