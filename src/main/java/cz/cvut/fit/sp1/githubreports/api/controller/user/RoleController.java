package cz.cvut.fit.sp1.githubreports.api.controller.user;

import cz.cvut.fit.sp1.githubreports.api.dto.user.RoleDTO;
import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.IncorrectRequestException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.NoEntityFoundException;
import cz.cvut.fit.sp1.githubreports.service.user.role.RoleConverter;
import cz.cvut.fit.sp1.githubreports.service.user.role.RoleSPI;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@AllArgsConstructor
@RestController
@RequestMapping("/roles")
public class RoleController {
    private final RoleSPI roleSPI;
    private final RoleConverter roleConverter;

    @GetMapping()
    public Collection<RoleDTO> getAll() {
        return roleConverter.fromModelsMany(roleSPI.readAll());
    }

    @GetMapping("/{id}")
    public RoleDTO getOne(@PathVariable String id) {
        return roleConverter.fromModel(roleSPI.readById(id).orElseThrow(NoEntityFoundException::new));
    }

    @PostMapping()
    public RoleDTO create(@RequestBody RoleDTO roleDTO) throws EntityStateException {
        return roleConverter.fromModel(roleSPI.create(roleConverter.toModel(roleDTO)));
    }

    @PutMapping("/{id}")
    public RoleDTO update(@PathVariable String id, @RequestBody RoleDTO roleDTO) throws IncorrectRequestException, EntityStateException {
        if (!roleDTO.getRoleName().equals(id))
            throw new IncorrectRequestException();
        return roleConverter.fromModel(roleSPI.update(roleDTO.getRoleName(), roleConverter.toModel(roleDTO)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        if (roleSPI.readById(id).isEmpty())
            throw new NoEntityFoundException();
        roleSPI.delete(id);
    }
}
