package cz.cvut.fit.sp1.githubreports.service.user.role;

import cz.cvut.fit.sp1.githubreports.api.dto.user.RoleDTO;
import cz.cvut.fit.sp1.githubreports.model.user.Role;
import cz.cvut.fit.sp1.githubreports.model.user.User;
import cz.cvut.fit.sp1.githubreports.service.user.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class RoleConverter {

    private final UserService userService;

    public Role toModel(RoleDTO roleDTO) {
        return new Role(
                roleDTO.getRoleName(),
                roleDTO.getUsersIDs().stream().
                        map(userId -> userService.readById(userId).orElseThrow(RuntimeException::new))
                        .collect(Collectors.toList())
        );
    }

    public RoleDTO fromModel(Role role) {
        return new RoleDTO(
                role.getRoleName(),
                role.getUsers().stream().map(User::getUserId).collect(Collectors.toList())
        );
    }

    public Collection<Role> toModelsMany(Collection<RoleDTO> userDTOS) {
        return userDTOS.stream().map(this::toModel).collect(Collectors.toList());
    }

    public Collection<RoleDTO> fromModelsMany(Collection<Role> roles) {
        return roles.stream().map(this::fromModel).collect(Collectors.toList());
    }

}
