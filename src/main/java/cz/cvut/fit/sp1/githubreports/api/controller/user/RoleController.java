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

    /**
         GET: "/roles"
         @return collection of all roles in our database.
     */
    @GetMapping()
    public Collection<RoleDTO> getAll() {
        return roleConverter.fromModelsMany(roleSPI.readAll());
    }

    /**
         GET: "/roles/{id}"
         @return role by id.
     */
    @GetMapping("/{id}")
    public RoleDTO getOne(@PathVariable String id) {
        return roleConverter.fromModel(roleSPI.readById(id).orElseThrow(NoEntityFoundException::new));
    }

    /**
         Create new role.

         POST: "/roles"

         Request body example:
         {
             "roleName": "ROLE_TRAINEE",
             "usersIDs": []
         }
         @return created role.
     */
    @PostMapping()
    public RoleDTO create(@RequestBody RoleDTO roleDTO) throws EntityStateException {
        return roleConverter.fromModel(roleSPI.create(roleConverter.toModel(roleDTO)));
    }

    /**
         Update existing role.

         PUT: "/roles/{roleName}"

         Request body example:
         {
             "roleName": "ROLE_TRAINEE",
             "usersIDs": []
         }
        @return updated role.
     */
    @PutMapping("/{roleName}")
    public RoleDTO update(@PathVariable String roleName, @RequestBody RoleDTO roleDTO) throws IncorrectRequestException, EntityStateException {
        if (!roleDTO.getRoleName().equals(roleName))
            throw new IncorrectRequestException();
        return roleConverter.fromModel(roleSPI.update(roleDTO.getRoleName(), roleConverter.toModel(roleDTO)));
    }

    /**
         Delete role.

         DELETE: "/roles/{roleName}"

         Request body example:
         {
             "roleName": "ROLE_TRAINEE",
             "usersIDs": []
         }
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        if (roleSPI.readById(id).isEmpty())
            throw new NoEntityFoundException();
        roleSPI.delete(id);
    }
}
