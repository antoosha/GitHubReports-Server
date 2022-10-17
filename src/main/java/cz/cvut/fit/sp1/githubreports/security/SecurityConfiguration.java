package cz.cvut.fit.sp1.githubreports.security;

import static org.springframework.security.config.Customizer.withDefaults;

import cz.cvut.fit.sp1.githubreports.security.filter.AppAuthenticationFilter;
import cz.cvut.fit.sp1.githubreports.security.filter.AppAuthorizationFilter;

import java.util.Arrays;

import cz.cvut.fit.sp1.githubreports.security.filter.FilterChainExceptionHandler;
import cz.cvut.fit.sp1.githubreports.service.jwt.JwtTokenSPI;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
@AllArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final AppUserDetailsServices userDetailsService;

    private final FilterChainExceptionHandler filterChainExceptionHandler;

    private final JwtTokenSPI jwtTokenSPI;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    /*
        URL for authentication - "/login"
        URL for refresh access token - "/users/token/refresh"
        Other URLs required access token.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeHttpRequests().antMatchers("/login/**", "/users/token/refresh/**").permitAll();
        http.authorizeHttpRequests().anyRequest().authenticated();

        http.addFilter(new AppAuthenticationFilter(authenticationManagerBean(), jwtTokenSPI));
        http.addFilterBefore(filterChainExceptionHandler, LogoutFilter.class);
        http.addFilterBefore(new AppAuthorizationFilter(jwtTokenSPI), UsernamePasswordAuthenticationFilter.class);

        // by default uses a Bean by the name of corsConfigurationSource
        http.cors(withDefaults());
    }

    // CORS configuration
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
