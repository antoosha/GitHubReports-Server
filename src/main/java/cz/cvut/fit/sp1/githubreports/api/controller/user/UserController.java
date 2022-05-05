package cz.cvut.fit.sp1.githubreports.api.controller.user;

import cz.cvut.fit.sp1.githubreports.api.dto.user.RoleDTO;
import cz.cvut.fit.sp1.githubreports.api.dto.user.UserDTO;
import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.IncorrectRequestException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.NoEntityFoundException;
import cz.cvut.fit.sp1.githubreports.service.user.user.UserConverter;
import cz.cvut.fit.sp1.githubreports.service.user.user.UserSPI;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final String secret;
    private final Integer expirationTimeAccessToken;
    private final UserSPI userSPI;
    private final UserConverter userConverter;

    public UserController(@Value("${my.secret}") String secret,
                          @Value("${expiration.time.access}") Integer expirationTimeAccessToken,
                          @Qualifier("UserService") UserSPI userSPI,
                          UserConverter userConverter) {
        this.secret = secret;
        this.expirationTimeAccessToken = expirationTimeAccessToken;
        this.userSPI = userSPI;
        this.userConverter = userConverter;
    }

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Collection<UserDTO> getAllUsers() {
        return userConverter.fromModelsMany(userSPI.readAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || @UserService.hasId(#id)")
    public UserDTO getOne(@PathVariable("id") Long id) {
        return userConverter.fromModel(userSPI.readById(id).orElseThrow(NoEntityFoundException::new));
    }

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserDTO create(@RequestBody UserDTO userDTO) throws EntityStateException {
        return userConverter.fromModel(userSPI.create(userConverter.toModel(userDTO)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || @UserService.hasId(#id)")
    public UserDTO update(@PathVariable Long id, @RequestBody UserDTO userDTO) throws IncorrectRequestException, EntityStateException {
        //System.out.println(userDTO.getUserId() + " " +  userDTO.getUsername());
        if (!userDTO.getUserId().equals(id))
            throw new IncorrectRequestException();
        return userConverter.fromModel(userSPI.update(userDTO.getUserId(), userConverter.toModel(userDTO)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(@PathVariable("id") Long id) {
        if (userSPI.readById(id).isEmpty())
            throw new NoEntityFoundException();
        userSPI.delete(id);
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        userSPI.refreshToken(request, response, secret, expirationTimeAccessToken);
    }

}
