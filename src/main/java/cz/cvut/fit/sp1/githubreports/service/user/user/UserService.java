package cz.cvut.fit.sp1.githubreports.service.user.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fit.sp1.githubreports.api.exceptions.AccessDeniedException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.HasRelationsException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.NoEntityFoundException;
import cz.cvut.fit.sp1.githubreports.dao.project.ProjectJpaRepository;
import cz.cvut.fit.sp1.githubreports.dao.user.UserJpaRepository;
import cz.cvut.fit.sp1.githubreports.model.user.Role;
import cz.cvut.fit.sp1.githubreports.model.user.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@AllArgsConstructor
@Transactional
@Service("UserService")
public class UserService implements UserSPI {

    private final PasswordEncoder passwordEncoder;

    private final UserJpaRepository userJpaRepository;
    private final ProjectJpaRepository projectJpaRepository;

    public boolean hasId(Long id) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return readByUsername(auth.getName()).orElseThrow(AccessDeniedException::new).getUserId().equals(id);
    }

    @Override
    public Collection<User> readAll() {
        return userJpaRepository.findAll();
    }

    @Override
    public Optional<User> readById(Long id) { return userJpaRepository.findById(id); }

    @Override
    public Optional<User> readByUsername(String username) {
        return userJpaRepository.findUserByUsername(username);
    }

    @Override
    public User create(User user) throws EntityStateException {
        if (userJpaRepository.findUserByUsername(user.getUsername()).isPresent() ||
                userJpaRepository.findUserByEmail(user.getEmail()).isPresent())
            throw new EntityStateException();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userJpaRepository.save(user);
    }

    @Override
    public User update(Long id, User user) throws EntityStateException {
        if (id == null || !userJpaRepository.existsById(id))
            throw new NoEntityFoundException();
        User userOld = userJpaRepository.getById(id);
        if (!userOld.getEmail().equals(user.getEmail()))
            if (userJpaRepository.findUserByEmail(user.getEmail()).isPresent())
                throw new EntityStateException();
        if (!userOld.getUsername().equals(user.getUsername()))
            if (userJpaRepository.findUserByUsername(user.getUsername()).isPresent())
                throw new EntityStateException();
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        /*
            If we update in User list of projects we should manually add this user to collection of users in project
            https://stackoverflow.com/questions/52203892/updating-manytomany-relationships-in-jpa-or-hibernate
        */
        user.getProjects().forEach(p->
            {
                if(p.getUsers().stream().noneMatch(u->u.getUserId().equals(id))) {
                    List<User> users = p.getUsers();
                    users.add(user);
                    p.setUsers(users);
                    projectJpaRepository.save(p);
                }
            }
        );


        return userJpaRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        if (userJpaRepository.existsById(id))
        {
            if(!userJpaRepository.getById(id).getProjects().isEmpty())
                throw new HasRelationsException();
            userJpaRepository.deleteById(id);
        }
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response, String secret, Integer expirationTimeAccessToken) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                User user = readByUsername(username).orElseThrow(RuntimeException::new);

                String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + expirationTimeAccessToken))
                        .withIssuer(request.getRequestURI())
                        .withClaim("roles", user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception e) {
                response.setHeader("error", e.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        }
        else {
            throw new ResponseStatusException(FORBIDDEN, "Refresh token wasn't found");
        }
    }

}
