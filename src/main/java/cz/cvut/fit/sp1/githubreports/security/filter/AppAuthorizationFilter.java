package cz.cvut.fit.sp1.githubreports.security.filter;

import cz.cvut.fit.sp1.githubreports.service.jwt.JwtTokenSPI;
import lombok.AllArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
public class AppAuthorizationFilter extends OncePerRequestFilter {

    private final JwtTokenSPI jwtTokenSPI;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/login") || request.getServletPath().equals("/users/token/refresh")) {
            filterChain.doFilter(request, response);
        }
        else {
            jwtTokenSPI.authorizeUser(request);
            filterChain.doFilter(request, response);
        }
    }
}
