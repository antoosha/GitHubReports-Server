package cz.cvut.fit.sp1.githubreports.api.delegate;

import cz.cvut.fit.sp1.githubreports.api.exceptions.NoEntityFoundException;
import cz.cvut.fit.sp1.githubreports.model.user.Role;
import cz.cvut.fit.sp1.githubreports.service.user.role.RoleMapper;
import cz.cvut.fit.sp1.githubreports.service.user.role.RoleSPI;
import lombok.RequiredArgsConstructor;
import org.openapi.api.RolesApi;
import org.openapi.model.RoleDTO;
import org.openapi.model.RoleSlimDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RoleApiDelegateImpl implements RolesApi {

    private final RoleSPI roleSPI;

    private final RoleMapper roleMapper;

    @Override
    public ResponseEntity<RoleDTO> getRole(String roleName) {
        Role role = roleSPI.readById(roleName);
        return ResponseEntity.ok(roleMapper.toDTO(role));
    }

    @Override
    public ResponseEntity<List<RoleSlimDTO>> getRoles() {
        return ResponseEntity.ok(roleMapper.toSlimDTOs(roleSPI.readAll().stream().toList()));
    }

}
