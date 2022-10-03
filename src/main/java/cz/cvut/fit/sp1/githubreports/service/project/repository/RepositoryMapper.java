package cz.cvut.fit.sp1.githubreports.service.project.repository;

import cz.cvut.fit.sp1.githubreports.model.project.Repository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.openapi.model.RepositoryDTO;

@Mapper
public interface RepositoryMapper {

    @Mapping(source = "project.projectId", target = "projectId")
    @Mapping(source = "commits", target = "commitsIds")
    RepositoryDTO fromModel(Repository repository);

    @Mapping(target = "project.projectId", source = "projectId")
    @Mapping(target = "commits", source = "commitsIds")
    Repository toModel(RepositoryDTO repositoryDTO);

}
