package cz.cvut.fit.sp1.githubreports.service.user.role;

import cz.cvut.fit.sp1.githubreports.dao.user.RoleJpaRepository;
import cz.cvut.fit.sp1.githubreports.model.user.Role;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@AllArgsConstructor
@Service
public class RoleService implements RoleSPI{

    private final RoleJpaRepository repository;

    @Override
    public Collection<Role> readAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Role> readById(String id) {
        return repository.findById(id);
    }

    @Override
    public void create(Role role) {
        repository.save(role);
    }

    @Override
    public void update(String id, Role role) {
        if (repository.existsById(id))
            repository.save(role);
    }

    @Override
    public void delete(String id) {
        if (repository.existsById(id))
            repository.deleteById(id);
    }
}
