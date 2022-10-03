package cz.cvut.fit.sp1.githubreports.service.project.commit;

import cz.cvut.fit.sp1.githubreports.model.project.Commit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.openapi.model.CommitDTO;

@Mapper
public interface CommitMapper {

    @Mapping(source = "repository.repositoryId", target = "repositoryId")
    @Mapping(source = "tags", target = "tagsIds")
    @Mapping(source = "comments", target = "commentsIds")
    CommitDTO fromModel(Commit commit);

    @Mapping(target = "repository.repositoryId", source = "repositoryId")
    @Mapping(target = "tags", source = "tagsIds")
    @Mapping(target = "comments", source = "commentsIds")
    Commit toModel(CommitDTO commitDTO);
}
