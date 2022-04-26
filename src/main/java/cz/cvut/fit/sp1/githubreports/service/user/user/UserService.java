package cz.cvut.fit.sp1.githubreports.service.user.user;

import cz.cvut.fit.sp1.githubreports.dao.user.UserJpaRepository;
import cz.cvut.fit.sp1.githubreports.model.user.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@AllArgsConstructor
@Transactional
@Service("UserService")
public class UserService implements UserSPI {

    private final PasswordEncoder passwordEncoder;

    private final UserJpaRepository repository;

    public boolean hasId(Long id) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return readByUsername(auth.getName()).orElseThrow(RuntimeException::new).getUserId().equals(id);
    }

    @Override
    public Collection<User> readAll() {
        return repository.findAll();
    }

    @Override
    public Optional<User> readById(Long id) { return repository.findById(id); }

    @Override
    public Optional<User> readByUsername(String username) {
        return repository.findUserByUsername(username);
    }

    @Override
    public void create(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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
