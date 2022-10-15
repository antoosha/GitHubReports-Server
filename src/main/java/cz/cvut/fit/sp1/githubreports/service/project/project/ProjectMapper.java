package cz.cvut.fit.sp1.githubreports.service.project.project;

import cz.cvut.fit.sp1.githubreports.model.project.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.openapi.model.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    ProjectDTO toDTO(Project project);

    ProjectSlimDTO toSlimDTO(Project project);

    @Mapping(target = "users", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "statistics", ignore = true)
    @Mapping(target = "repositories", ignore = true)
    @Mapping(target = "projectId", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "author", ignore = true)
    Project fromUpdateSlimDTO(ProjectUpdateSlimDTO projectUpdateSlimDTO);

    @Mapping(target = "users", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "statistics", ignore = true)
    @Mapping(target = "repositories", ignore = true)
    @Mapping(target = "author", ignore = true)
    Project fromSlimDTO(ProjectSlimDTO projectSlimDTO);

    List<ProjectDTO> toDTOs(List<Project> projects);

    List<ProjectSlimDTO> toSlimDTOs(List<Project> projects);

}
