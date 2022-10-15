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

    User readById(Long id);

    User readByUsername(String username);

    User readUserFromToken();

    User create(User user) throws EntityStateException;

    User update(String username, User updatedUser);

    User changePassword(String username, String password);

    User addRole(String username, String roleName);

    User removeRole(String username, String roleName);

    void delete(String username);

    Collection<Project> getAllUserProjects(String username);

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

    void savePhoto(String username, MultipartFile multipartFile);

    byte[] getImage(String username);

}
