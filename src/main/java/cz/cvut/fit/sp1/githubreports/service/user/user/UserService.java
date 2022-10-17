package cz.cvut.fit.sp1.githubreports.service.user.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fit.sp1.githubreports.api.exceptions.*;
import cz.cvut.fit.sp1.githubreports.dao.user.RoleJpaRepository;
import cz.cvut.fit.sp1.githubreports.dao.user.UserJpaRepository;
import cz.cvut.fit.sp1.githubreports.model.project.Project;
import cz.cvut.fit.sp1.githubreports.model.user.Role;
import cz.cvut.fit.sp1.githubreports.model.user.User;
import cz.cvut.fit.sp1.githubreports.service.jwt.JwtTokenSPI;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@Transactional
@Service("UserService")
public class UserService implements UserSPI {

    private final PasswordEncoder passwordEncoder;

    private final UserJpaRepository userJpaRepository;

    private final RoleJpaRepository roleJpaRepository;

    private String baseURL = "localhost:8080"; //TODO


    private void delegateUserCommentsToDeletedUser(User toDeleteUser, User deletedUser) {
        toDeleteUser.getComments()
                .forEach(comment -> comment.setAuthor(deletedUser));
    }

    private void removeAllRelationProjects(User toDeleteUser) {
        toDeleteUser.getProjects().forEach(project -> {
            List<User> users = project.getUsers();
            users.remove(toDeleteUser);
            project.setUsers(users);
        });
    }

    public boolean hasId(Long id) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return readByUsername(auth.getName()).getUserId().equals(id);
    }

    public boolean hasUsername(String username) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return readByUsername(auth.getName()).getUsername().equals(username);
    }

    @Override
    public Collection<User> readAll() {
        return userJpaRepository.findAll();
    }

    @Override
    public User readById(Long id) {
        return userJpaRepository.findById(id).orElseThrow(NoEntityFoundException::new);
    }

    @Override
    public User readByUsername(String username) {
        return userJpaRepository.findUserByUsername(username).orElseThrow(NoEntityFoundException::new);
    }

    @Override
    public User readUserFromToken() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userJpaRepository.findUserByUsername(username).orElseThrow(NoEntityFoundException::new);
    }

    private boolean correctPassword(String password) {
        if (password.length() < 8) {
            return false;
        }
        //place for password regex expression
        return true;
    }

    @Override
    public User create(User user) throws EntityStateException {
        if (userJpaRepository.findUserByUsername(user.getUsername()).isPresent() ||
                userJpaRepository.findUserByEmail(user.getEmail()).isPresent())
            throw new EntityStateException();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setProfilePhotoURL(baseURL + "/" + user.getUsername() + "/photos");
        return userJpaRepository.save(user);
    }

    public User update(String username, User updatedUser) {
        if (username == null)
            throw new IncorrectRequestException();

        User userOld = userJpaRepository.findUserByUsername(username).orElseThrow(NoEntityFoundException::new);

        // email address has been changed
        if (!userOld.getEmail().equals(updatedUser.getEmail())) {
            if (userJpaRepository.findUserByEmail(updatedUser.getEmail()).isPresent()) {
                throw new EntityStateException();
            }
        }
        // username has been changed
        if (!userOld.getUsername().equals(updatedUser.getUsername())) {
            if (userJpaRepository.findUserByUsername(updatedUser.getUsername()).isPresent()) {
                throw new EntityStateException();
            }
        }

        userOld.setEmail(updatedUser.getEmail()); // update email
        userOld.setUsername(updatedUser.getUsername());
        userOld.getComments().forEach(comment -> comment.setAuthorUsername(userOld.getUsername()));
        userOld.setProfilePhotoURL(baseURL + "/" + userOld.getUsername() + "/photo");
        return userJpaRepository.save(userOld);
    }

    @Override
    public User changePassword(String username, String password) {
        if (!correctPassword(password)) {
            throw new EntityStateException();
        }
        User user = userJpaRepository.findUserByUsername(username).orElseThrow(NoEntityFoundException::new);
        user.setPassword(passwordEncoder.encode(password));
        return userJpaRepository.save(user);
    }

    @Override
    public User addRole(String username, String roleName) {
        User user = userJpaRepository.findUserByUsername(username).orElseThrow(NoEntityFoundException::new);
        Role role = roleJpaRepository.findById(roleName).orElseThrow(NoEntityFoundException::new);
        user.getRoles().add(role);
        return userJpaRepository.save(user);
    }

    @Override
    public User removeRole(String username, String roleName) {
        User user = userJpaRepository.findUserByUsername(username).orElseThrow(NoEntityFoundException::new);
        Role role = roleJpaRepository.findById(roleName).orElseThrow(NoEntityFoundException::new);
        user.getRoles().remove(role);
        return userJpaRepository.save(user);
    }

    @Override
    public void delete(String username) {
        User userToDelete = userJpaRepository.findUserByUsername(username).orElseThrow(NoEntityFoundException::new);
        if (!userToDelete.getCreatedProjects().isEmpty())
            throw new HasRelationsException();
        User deletedUser = userJpaRepository.findUserByUsername("deletedUser").orElseThrow(NoEntityFoundException::new);
        delegateUserCommentsToDeletedUser(userToDelete, deletedUser);
        removeAllRelationProjects(userToDelete);
        userJpaRepository.delete(userToDelete);
    }

    @Override
    public Collection<Project> getAllUserProjects(String username) {
        User user = userJpaRepository.findUserByUsername(username).orElseThrow(NoEntityFoundException::new);
        return user.getProjects();
    }

    @Override
    public void savePhoto(String username, MultipartFile multipartFile) throws NoEntityFoundException {
        if (multipartFile == null || username == null || username.isEmpty())
            throw new IncorrectRequestException();
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        User user = userJpaRepository.findUserByUsername(username).orElseThrow(NoEntityFoundException::new);
        String uploadDir = "src/main/resources/serverData/images/userPhotos/" + user.getUserId();
        saveFile(uploadDir, "photo." + fileName.split("\\.")[1], multipartFile);
        userJpaRepository.save(user);
    }

    @Override
    public byte[] getImage(String username) {
        User user = userJpaRepository.findUserByUsername(username).orElseThrow(NoEntityFoundException::new);
        InputStream is = null;
        byte[] bytes = null;
        try {
            is = new FileInputStream("src/main/resources/serverData/images/userPhotos/" + user.getUserId() + "/photo.jpg");
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


    private void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws EntityStateException {
        Path uploadPath = Paths.get(uploadDir);
        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
        } catch (IOException ioe) {
            throw new EntityStateException("Could not create directory image file");
        }
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new EntityStateException("Could not save image file: " + fileName);
        }
    }
}
