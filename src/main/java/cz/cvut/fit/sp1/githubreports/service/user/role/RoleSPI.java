package cz.cvut.fit.sp1.githubreports.service.user.role;


import cz.cvut.fit.sp1.githubreports.model.user.Role;

import java.util.Collection;
import java.util.Optional;

public interface RoleSPI {

    Collection<Role> readRoles();
    Optional<Role> readRoleById(String id);
    void createRole(Role role);
    void updateRole(String id, Role role);
    void deleteRole(String id);

}
