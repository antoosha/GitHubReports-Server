package cz.cvut.fit.sp1.githubreports.api.controller.user;

import cz.cvut.fit.sp1.githubreports.api.dto.project.ProjectDTO;
import cz.cvut.fit.sp1.githubreports.api.dto.user.UserDTO;
import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.IncorrectRequestException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.NoEntityFoundException;
import cz.cvut.fit.sp1.githubreports.service.project.project.ProjectConverter;
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

    /**
     *  "secret" - secrete key for sign the tokens
     *  "expirationTimeAccessToken" - expiration time of token in milliseconds
     */
    private final String secret;
    private final Integer expirationTimeAccessToken;
    private final UserSPI userSPI;
    private final UserConverter userConverter;
    private final ProjectConverter projectConverter;

    public UserController(@Value("${my.secret}") String secret,
                          @Value("${expiration.time.access}") Integer expirationTimeAccessToken,
                          @Qualifier("UserService") UserSPI userSPI,
                          UserConverter userConverter, ProjectConverter projectConverter) {
        this.secret = secret;
        this.expirationTimeAccessToken = expirationTimeAccessToken;
        this.userSPI = userSPI;
        this.userConverter = userConverter;
        this.projectConverter = projectConverter;
    }

    /**
        GET: "/users"
        @return collection of all users in our database.
     */
    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Collection<UserDTO> getAll() {
        return userConverter.fromModelsMany(userSPI.readAll());
    }

    /**
     * Get all user's projects. Client must have admin role for this method or call this method with his id.
     * GET: "/users/{id}/projects"
     *
     * @return collection of user's projects.
     */
    @GetMapping("/{id}/projects")
    @PreAuthorize("hasRole('ROLE_ADMIN') || @UserService.hasId(#id)")
    public Collection<ProjectDTO> getAllUserProjects(@PathVariable("id") Long id) {
        return projectConverter.fromModelsMany(userSPI.getAllUserProjects(id));
    }

    /**
     * Get one user. Client must have admin role for this method or call this method with his username.
     * GET: "/username/{username}"
     *
     * @return user with by username.
     */
    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || @UserService.hasUsername(#username)")
    public UserDTO getOneByUsername(@PathVariable("username") String username) {
        return userConverter.fromModel(userSPI.readByUsername(username).orElseThrow(NoEntityFoundException::new));
    }

    /**
         Get one user. Client must have admin role for this method or call this method with his id.
         GET: "/users/{id}"
         @return user by id.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || @UserService.hasId(#id)")
    public UserDTO getOne(@PathVariable("id") Long id) {
        return userConverter.fromModel(userSPI.readById(id).orElseThrow(NoEntityFoundException::new));
    }

    /**
     * POST: "/users"
     * <p>
     * Request body example (only required parameters):
     * {
     * "username": "username",
     * "email": "userMail",
     * "password": "pass",
     * "pathToFileWithPhoto": "photo",
     * "commentsIDs": [],
     * "projectsIDs": [],
     * "createdProjectsIDs": [],
     * "statisticsIDs": [],
     * "rolesIDs": [
     * "ROLE_DEVELOPER"
     * ]
     * }
     *
     * @return created user.
     */
    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserDTO create(@RequestBody UserDTO userDTO) throws EntityStateException {
        return userConverter.fromModel(userSPI.create(userConverter.toModel(userDTO)));
    }

    /**
     Update user by id. Client must have admin role for this method or call this method with his id.
     PUT: "/users/{id}"

     Request body example (only required parameters):
     {
     "userId": 1,
     "username": "username",
     "email": "userMail",
     "password": "pass",
     "pathToFileWithPhoto": "photo",
     "commentsIDs": [],
     "projectsIDs": [],
     "createdProjectsIDs": [],
     "statisticsIDs": [],
     "rolesIDs": [
     "ROLE_DEVELOPER"
     ]
     }
     @return updated user.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || @UserService.hasId(#id)")
    public UserDTO update(@PathVariable Long id, @RequestBody UserDTO userDTO) throws IncorrectRequestException, EntityStateException {
        //System.out.println(userDTO.getUserId() + " " +  userDTO.getUsername());
        if (!userDTO.getUserId().equals(id))
            throw new IncorrectRequestException();
        return userConverter.fromModel(userSPI.update(userDTO.getUserId(), userConverter.toModel(userDTO)));
    }

    /**
        Delete user by id. Client must have admin role for this method.
        DELETE: "/users/{id}"
        @param id
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(@PathVariable("id") Long id) {
        if (userSPI.readById(id).isEmpty())
            throw new NoEntityFoundException();
        userSPI.delete(id);
    }

    /**
         Get new access token.
         GET: "/users/token/refresh"
     */
    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        userSPI.refreshToken(request, response, secret, expirationTimeAccessToken);
    }

}
