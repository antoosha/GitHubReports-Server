package cz.cvut.fit.sp1.githubreports.api.delegate;

import cz.cvut.fit.sp1.githubreports.api.exceptions.NoEntityFoundException;
import cz.cvut.fit.sp1.githubreports.model.user.User;
import cz.cvut.fit.sp1.githubreports.service.user.user.UserMapper;
import cz.cvut.fit.sp1.githubreports.service.user.user.UserSPI;
import org.openapi.api.UsersApi;
import org.openapi.model.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
public class UserApiDelegateImpl implements UsersApi {

    private final UserSPI userSPI;

    private final UserMapper userMapper;

    public UserApiDelegateImpl(UserSPI userSPI,
                               UserMapper userMapper) {
        this.userSPI = userSPI;
        this.userMapper = userMapper;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN') || @UserService.hasId(#id)")
    public ResponseEntity<UserDTO> getUser(Long id) {
        User user = userSPI.readById(id).orElseThrow(NoEntityFoundException::new);
        return ResponseEntity.ok(userMapper.toDTO(user));
    }

}
