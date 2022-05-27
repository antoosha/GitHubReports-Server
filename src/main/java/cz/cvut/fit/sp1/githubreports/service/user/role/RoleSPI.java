package cz.cvut.fit.sp1.githubreports.service.user.role;


import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.model.user.Role;

import java.util.Collection;
import java.util.Optional;

public interface RoleSPI {

    Collection<Role> readAll();

    Optional<Role> readById(String id);

    Role create(Role role) throws EntityStateException;

    Role update(String id, Role role) throws EntityStateException;

    void delete(String id);

}
