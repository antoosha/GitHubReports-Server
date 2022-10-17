package cz.cvut.fit.sp1.githubreports.api.delegate;

import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.NoEntityFoundException;
import cz.cvut.fit.sp1.githubreports.model.project.Project;
import cz.cvut.fit.sp1.githubreports.model.user.User;
import cz.cvut.fit.sp1.githubreports.service.jwt.JwtTokenSPI;
import cz.cvut.fit.sp1.githubreports.service.project.project.ProjectMapper;
import cz.cvut.fit.sp1.githubreports.service.user.user.UserMapper;
import cz.cvut.fit.sp1.githubreports.service.user.user.UserSPI;
import lombok.RequiredArgsConstructor;
import org.openapi.api.UsersApi;
import org.openapi.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserApiDelegateImpl implements UsersApi {

    private final UserSPI userSPI;

    private final JwtTokenSPI jwtTokenSPI;

    private final UserMapper userMapper;

    private final ProjectMapper projectMapper;

    @Autowired
    private final HttpServletRequest request;

    @Autowired
    private final HttpServletResponse response;

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN') || @UserService.hasUsername(#username)")
    public ResponseEntity<UserDTO> getUser(String username) {
        UserDTO userDTO = userMapper.toDTO(userSPI.readByUsername(username));
        return ResponseEntity.ok(userDTO);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDTO> addRole(String username, String roleName) {
        UserDTO userDTO = userMapper.toDTO(userSPI.addRole(username, roleName));
        return ResponseEntity.ok(userDTO);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN') || @UserService.hasUsername(#username)")
    public ResponseEntity<List<ProjectDTO>> getUserProjects(String username) {
        List<Project> projects = userSPI.getAllUserProjects(username).stream().toList();
        return ResponseEntity.ok(projectMapper.toDTOs(projects));
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserSlimDTO>> getUsers() {
        return ResponseEntity.ok(userMapper.toSlimDTOs(userSPI.readAll().stream().toList()));
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDTO> deleteRole(String username, String roleName) {
        UserDTO userDTO = userMapper.toDTO(userSPI.removeRole(username, roleName));
        return ResponseEntity.ok(userDTO);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDTO> createUser(UserCreateSlimDTO userSlimDTO) {
        User userToCreate = userMapper.fromCreateSlimDTO(userSlimDTO);
        return ResponseEntity.ok(userMapper.toDTO(userSPI.create(userToCreate)));
    }

    /**
     * After update, you need to update your access and refresh tokens!!!
     * @param username username of user to update (required)
     * @param userSlimDTO  (required)
     */
    @Override
    @PreAuthorize("@UserService.hasUsername(#username)")
    public ResponseEntity<UserDTO> updateUser(String username, UserUpdateSlimDTO userSlimDTO) {
        User updatedUser = userMapper.fromUpdateSlimDTO(userSlimDTO);
        return ResponseEntity.ok(userMapper.toDTO(userSPI.update(username, updatedUser)));
    }

    @Override
    @PreAuthorize("@UserService.hasUsername(#username)")
    public ResponseEntity<Void> changePassword(String username, PasswordDTO passwordDTO) {
        userSPI.changePassword(username, passwordDTO.getPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteUser(String username) {
        userSPI.delete(username);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @PreAuthorize("@UserService.hasUsername(#username)")
    public ResponseEntity<Void> saveUserPhoto(String username, MultipartFile photo) {
        userSPI.savePhoto(username, photo);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Resource> getImageWithMediaType(String username) {
        ByteArrayResource resource = new ByteArrayResource(userSPI.getImage(username));
        return ResponseEntity.ok(resource);
    }

    @Override
    public ResponseEntity<TokensDTO> refreshToken() {
        return ResponseEntity.ok(jwtTokenSPI.getRefreshToken(request));
    }

}
