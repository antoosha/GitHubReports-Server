package cz.cvut.fit.sp1.githubreports.service.user.role;

import cz.cvut.fit.sp1.githubreports.model.user.Role;

import java.util.Collection;
import java.util.Optional;

public interface RoleSPI {

    Collection<Role> read();

    Optional<Role> readById(String id);

    void create(Role role);

    void update(String id, Role role);

    void delete(String id);

}
