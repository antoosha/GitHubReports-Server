package cz.cvut.fit.sp1.githubreports.service.user.user;

import cz.cvut.fit.sp1.githubreports.dao.user.UserJpaRepository;
import cz.cvut.fit.sp1.githubreports.model.user.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@AllArgsConstructor
@Service("UserService")
public class UserService implements UserSPI {

    private final UserJpaRepository repository;

    @Override
    public Collection<User> readUsers() {
        return repository.findAll();
    }

    @Override
    public Optional<User> readUserById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<User> readUserByUsername(String username) {
        return repository.findUserByUsername(username);
    }

    @Override
    public void createUser(User user) {
        repository.save(user);
    }

    @Override
    public void updateUser(Long id, User user) {
        if (repository.existsById(id))
            repository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        if (repository.existsById(id))
            repository.deleteById(id);
    }

}
