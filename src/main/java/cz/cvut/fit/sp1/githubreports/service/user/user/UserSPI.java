package cz.cvut.fit.sp1.githubreports.service.user.user;

import cz.cvut.fit.sp1.githubreports.model.user.User;

import java.util.Collection;
import java.util.Optional;

public interface UserSPI {

    Collection<User> readAll();

    Optional<User> readById(Long id);

    Optional<User> readByUsername(String username);

    void create(User user);

    void update(Long id, User user);

    void delete(Long id);

}
