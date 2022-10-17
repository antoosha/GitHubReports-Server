package cz.cvut.fit.sp1.githubreports.service.jwt;

import cz.cvut.fit.sp1.githubreports.security.AppUserDetails;
import org.openapi.model.TokensDTO;

import javax.servlet.http.HttpServletRequest;

public interface JwtTokenSPI {

    String getAccessToken(AppUserDetails user);

    String getRefreshToken(AppUserDetails user);

    TokensDTO getRefreshToken(HttpServletRequest request);

    void authorizeUser(HttpServletRequest request);

}
