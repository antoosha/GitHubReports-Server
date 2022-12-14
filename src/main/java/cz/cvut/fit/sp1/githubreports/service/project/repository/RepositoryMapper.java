package cz.cvut.fit.sp1.githubreports.service.project.repository;

import cz.cvut.fit.sp1.githubreports.model.project.Repository;
import cz.cvut.fit.sp1.githubreports.model.project.Repository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.openapi.model.RepositoryDTO;
import org.openapi.model.RepositorySlimDTO;
import org.openapi.model.RepositoryUpdateSlimDTO;
import org.openapi.model.RepositoryDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RepositoryMapper {

    RepositoryDTO toDTO(Repository repository);

    RepositorySlimDTO toSlimDTO(Repository repository);

    @Mapping(target = "project", ignore = true)
    @Mapping(target = "commits", ignore = true)
    Repository fromSlimDTO(RepositorySlimDTO repositorySlimDTO);

    @Mapping(target = "repositoryURL", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "commits", ignore = true)
    Repository fromUpdateSlimDTO(RepositoryUpdateSlimDTO repositoryUpdateSlimDTO);

    List<RepositoryDTO> toDTOs(List<Repository> repositorys);

    List<RepositorySlimDTO> toSlimDTOs(List<Repository> repositorys);
}
