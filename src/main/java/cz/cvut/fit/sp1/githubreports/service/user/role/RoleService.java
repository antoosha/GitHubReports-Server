package cz.cvut.fit.sp1.githubreports.service.user.role;

import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.dao.user.RoleJpaRepository;
import cz.cvut.fit.sp1.githubreports.model.user.Role;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.Optional;

@AllArgsConstructor
@Service
public class RoleService implements RoleSPI {

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
    public Role create(Role role) throws EntityStateException {
        if (repository.existsById(role.getRoleName())) throw new EntityStateException();
        return repository.save(role);
    }

    @Override
    public Role update(String id, Role role) throws EntityStateException {
        if (!repository.existsById(id)) throw new EntityStateException();
        return repository.save(role);
    }

    @Override
    public void delete(String id) {
        if (repository.existsById(id))
            repository.deleteById(id);
        else throw new EntityNotFoundException();
    }
}
