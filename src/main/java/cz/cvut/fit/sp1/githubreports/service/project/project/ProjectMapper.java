package cz.cvut.fit.sp1.githubreports.service.project.project;

import cz.cvut.fit.sp1.githubreports.model.project.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.openapi.model.ProjectDTO;

@Mapper
public interface ProjectMapper {

    @Mapping(source = "author.userId", target = "authorId")
    @Mapping(source = "repositories", target = "repositoriesIds")
    @Mapping(source = "statistics", target = "statisticsIds")
    @Mapping(source = "users", target = "usersIds")
    @Mapping(source = "tags", target = "tagsIds")
    ProjectDTO fromModel(Project project);

    @Mapping(target = "author.userId", source = "authorId")
    @Mapping(target = "repositories", source = "repositoriesIds")
    @Mapping(target = "statistics", source = "statisticsIds")
    @Mapping(target = "users", source = "usersIds")
    @Mapping(target = "tags", source = "tagsIds")
    Project toModel(ProjectDTO projectDTO);
}
