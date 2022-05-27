package cz.cvut.fit.sp1.githubreports.service.user.user;

import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.model.project.Project;
import cz.cvut.fit.sp1.githubreports.model.user.User;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

public interface UserSPI {

    Collection<User> readAll();

    Optional<User> readById(Long id);

    Optional<User> readByUsername(String username);

    User create(User user) throws EntityStateException;

    User WithPassword(Long id, User user) throws EntityStateException;

    User updateWithoutPassword(Long id, User user) throws EntityStateException;

    void delete(Long id);

    Collection<Project> getAllUserProjects(Long id);

    void refreshToken(HttpServletRequest request, HttpServletResponse response, String secret, Integer expirationTimeAccessToken) throws IOException;

    void savePhoto(String username, MultipartFile multipartFile);

}
