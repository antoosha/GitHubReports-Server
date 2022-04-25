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
    public Collection<User> readAll() {
        return repository.findAll();
    }

    @Override
    public Optional<User> readById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<User> readByUsername(String username) {
        return repository.findUserByUsername(username);
    }

    @Override
    public void create(User user) {
        repository.save(user);
    }

    @Override
    public void update(Long id, User user) {
        if (repository.existsById(id))
            repository.save(user);
    }

    @Override
    public void delete(Long id) {
        if (repository.existsById(id))
            repository.deleteById(id);
    }

}
