package cz.cvut.fit.sp1.githubreports.service.user.role;

import cz.cvut.fit.sp1.githubreports.model.user.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.openapi.model.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleDTO toDTO(Role role);

    RoleSlimDTO toSlimDTO(Role role);

    @Mapping(target = "users", ignore = true)
    Role fromSlimDTO(RoleSlimDTO roleSlimDTO);

    List<RoleDTO> toDTOs(List<Role> roles);

    List<RoleSlimDTO> toSlimDTOs(List<Role> roles);

}
