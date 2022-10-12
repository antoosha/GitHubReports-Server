package cz.cvut.fit.sp1.githubreports.service.user.user;

import cz.cvut.fit.sp1.githubreports.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.openapi.model.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User user);

    UserSlimDTO toSlimDTO(User user);

    @Mapping(target = "statistics", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "projects", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdProjects", ignore = true)
    @Mapping(target = "comments", ignore = true)
    User fromSlimDTO(UserSlimDTO userSlimDTO);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "statistics", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "projects", ignore = true)
    @Mapping(target = "profilePhotoURL", ignore = true)
    @Mapping(target = "createdProjects", ignore = true)
    @Mapping(target = "comments", ignore = true)
    User fromCreateSlimDTO(UserCreateSlimDTO userCreateSlimDTO);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "statistics", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "projects", ignore = true)
    @Mapping(target = "profilePhotoURL", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdProjects", ignore = true)
    @Mapping(target = "comments", ignore = true)
    User fromUpdateSlimDTO(UserUpdateSlimDTO userUpdateSlimDTO);

    List<UserDTO> toDTOs(List<User> users);

    List<UserSlimDTO> toSlimDTOs(List<User> users);

}
