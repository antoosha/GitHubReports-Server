package cz.cvut.fit.sp1.githubreports.api.controller.user;

import cz.cvut.fit.sp1.githubreports.api.dto.project.ProjectDTO;
import cz.cvut.fit.sp1.githubreports.api.dto.user.UserDTO;
import cz.cvut.fit.sp1.githubreports.api.dto.user.UserUpdateDTO;
import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.IncorrectRequestException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.NoEntityFoundException;
import cz.cvut.fit.sp1.githubreports.model.user.User;
import cz.cvut.fit.sp1.githubreports.service.project.project.ProjectConverter;
import cz.cvut.fit.sp1.githubreports.service.user.user.UserConverter;
import cz.cvut.fit.sp1.githubreports.service.user.user.UserSPI;
import cz.cvut.fit.sp1.githubreports.service.user.user.UserUpdateConverter;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
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
    private final UserUpdateConverter userUpdateConverter;
    private final ProjectConverter projectConverter;

    public UserController(@Value("${my.secret}") String secret,
                          @Value("${expiration.time.access}") Integer expirationTimeAccessToken,
                          @Qualifier("UserService") UserSPI userSPI,
                          UserConverter userConverter,
                          UserUpdateConverter userUpdateConverter,
                          ProjectConverter projectConverter) {
        this.secret = secret;
        this.expirationTimeAccessToken = expirationTimeAccessToken;
        this.userSPI = userSPI;
        this.userConverter = userConverter;
        this.userUpdateConverter = userUpdateConverter;
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
        if (!userDTO.getUserId().equals(id))
            throw new IncorrectRequestException();
        return userConverter.fromModel(userSPI.update(userDTO.getUserId(), userConverter.toModel(userDTO)));
    }

    /**
     Update user by id without changing the password . Client must have admin role for this method or call this method with his id.
     PUT: "/users/{id}"

     Request body example (only required parameters):
     {
     "userId": 1,
     "username": "username",
     "email": "userMail",
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
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || @UserService.hasId(#id)")
    public UserUpdateDTO update(@PathVariable Long id, @RequestBody UserUpdateDTO userUpdateDTO) throws IncorrectRequestException, EntityStateException {
        if (!userUpdateDTO.getUserId().equals(id))
            throw new IncorrectRequestException();
        return userUpdateConverter.fromModel(userSPI.updateWithoutPassword(userUpdateDTO.getUserId(), userUpdateConverter.toModel(userUpdateDTO)));
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
     * Get new access token.
     * GET: "/users/token/refresh"
     */
    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            userSPI.refreshToken(request, response, secret, expirationTimeAccessToken);
        } catch (IOException e) {
            throw new EntityStateException(e.getMessage());
        }
    }

    /**
     * Save user's photo
     * ONLY .jpg
     *
     * @param username
     * @param multipartFile
     * @throws EntityStateException
     */
    @PostMapping("/{username}/save/photo")
    public void saveUserPhoto(@PathVariable("username") String username, @RequestParam("photo") MultipartFile multipartFile) throws EntityStateException {
        userSPI.savePhoto(username, multipartFile);
    }

    /**
     * Get user's photo
     * @param id
     * @return photo in jpg format
     * @throws EntityStateException
     */
    @GetMapping(
            value = "/{id}/photo",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody byte[] getImageWithMediaType(@PathVariable("id") Long id) throws EntityStateException {
        InputStream is = null;
        byte[] bytes = null;
        try {
            is = new FileInputStream("src/main/resources/serverData/images/userPhotos/" + id + "/photo.jpg");
        } catch (FileNotFoundException e) {
            throw new NoEntityFoundException();
        }
        try {
            bytes = IOUtils.toByteArray(is);
        } catch (IOException ioe) {
            throw new EntityStateException("Problem with sending response bytes");
        }
        return bytes;
    }

}
