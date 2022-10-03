package cz.cvut.fit.sp1.githubreports.service.user.role;

import cz.cvut.fit.sp1.githubreports.model.user.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.openapi.model.RoleDTO;

@Mapper
public interface RoleMapper {

    @Mapping(source = "users", target = "usersIds")
    RoleDTO fromModel(Role role);

    @Mapping(target = "users", source = "usersIds")
    Role toModel(RoleDTO roleDTO);
}
