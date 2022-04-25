package cz.cvut.fit.sp1.githubreports.service.user.user;

import cz.cvut.fit.sp1.githubreports.model.user.User;

import java.util.Collection;
import java.util.Optional;

public interface UserSPI {

    Collection<User> readUsers();
    Optional<User> readUserById(Long id);
    Optional<User> readUserByUsername(String username);
    void createUser(User user);
    void updateUser(Long id, User user);
    void deleteUser(Long id);

}
