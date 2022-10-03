package cz.cvut.fit.sp1.githubreports.service.user.user;

import cz.cvut.fit.sp1.githubreports.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.openapi.model.UserDTO;

@Mapper
public interface UserMapper {

    @Mapping(source = "comments", target = "commentsIds")
    @Mapping(source = "projects", target = "projectsIds")
    @Mapping(source = "createdProjects", target = "createdProjectsIds")
    @Mapping(source = "statistics", target = "statisticsIds")
    @Mapping(source = "roles", target = "rolesIds")
    UserDTO fromModel(User user);

    @Mapping(target = "comments", source = "commentsIds")
    @Mapping(target = "projects", source = "projectsIds")
    @Mapping(target = "createdProjects", source = "createdProjectsIds")
    @Mapping(target = "statistics", source = "statisticsIds")
    @Mapping(target = "roles", source = "rolesIds")
    User toModel(UserDTO userDTO);
}
