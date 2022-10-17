package cz.cvut.fit.sp1.githubreports.service.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import cz.cvut.fit.sp1.githubreports.api.exceptions.AccessDeniedException;
import cz.cvut.fit.sp1.githubreports.security.AppUserDetails;
import cz.cvut.fit.sp1.githubreports.service.user.user.UserSPI;
import lombok.RequiredArgsConstructor;
import org.openapi.model.TokensDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@RequiredArgsConstructor
public class JwtTokenService implements JwtTokenSPI {

    @Value("${expiration.time.access}")
    private Integer expirationTimeAccessToken;

    @Value("${expiration.time.refresh}")
    private Integer expirationTimeRefreshToken;

    private final Algorithm algorithm;

    private final UserSPI userSPI;

    private DecodedJWT getDecodedJWT(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new AccessDeniedException("Missing authorization header, that must starts with \"Bearer \".");
        }
        DecodedJWT decodedJWT;
        try {
            String token = authorizationHeader.substring("Bearer ".length());
            JWTVerifier verifier = JWT.require(algorithm).build();
            decodedJWT = verifier.verify(token);
        } catch (Exception e) {
            throw new AccessDeniedException("Wrong token.");
        }
        return decodedJWT;
    }

    @Override
    public String getAccessToken(AppUserDetails user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTimeAccessToken))
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .sign(algorithm);
    }

    @Override
    public String getRefreshToken(AppUserDetails user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTimeRefreshToken))
                .sign(algorithm);
    }

    @Override
    public TokensDTO getRefreshToken(HttpServletRequest request) {
        String username = getDecodedJWT(request.getHeader(AUTHORIZATION)).getSubject();
        AppUserDetails user = new AppUserDetails(userSPI.readByUsername(username));

        TokensDTO tokensDTO = new TokensDTO();
        tokensDTO.setAccessToken(getAccessToken(user));
        tokensDTO.setRefreshToken(getRefreshToken(user));

        return tokensDTO;
    }

    @Override
    public void authorizeUser(HttpServletRequest request) {
        DecodedJWT decodedJWT = getDecodedJWT(request.getHeader(AUTHORIZATION));
        String username = decodedJWT.getSubject();

        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

}
