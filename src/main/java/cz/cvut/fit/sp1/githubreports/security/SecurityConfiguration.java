package cz.cvut.fit.sp1.githubreports.security;

import static org.springframework.security.config.Customizer.withDefaults;

import cz.cvut.fit.sp1.githubreports.security.filter.AppAuthenticationFilter;
import cz.cvut.fit.sp1.githubreports.security.filter.AppAuthorizationFilter;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final String secret;
    private final Integer expirationTimeAccessToken;
    private final Integer expirationTimeRefreshToken;
    private final AppUserDetailsServices userDetailsService;

    public SecurityConfiguration(@Value("${my.secret}") String secret,
                                 @Value("${expiration.time.access}") Integer expirationTimeAccessToken,
                                 @Value("${expiration.time.refresh}") Integer expirationTimeRefreshToken,
                                 AppUserDetailsServices userDetailsService) {
        this.secret = secret;
        this.expirationTimeAccessToken = expirationTimeAccessToken;
        this.expirationTimeRefreshToken = expirationTimeRefreshToken;
        this.userDetailsService = userDetailsService;
    }

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
        AppAuthenticationFilter appAuthenticationFilter =
                new AppAuthenticationFilter(secret, expirationTimeAccessToken, expirationTimeRefreshToken, authenticationManagerBean());
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeHttpRequests().antMatchers("/login/**", "/users/token/refresh/**").permitAll();
        http.authorizeHttpRequests().anyRequest().permitAll();
        http.addFilter(appAuthenticationFilter);
        http.addFilterBefore(new AppAuthorizationFilter(secret), UsernamePasswordAuthenticationFilter.class);

        // by default uses a Bean by the name of corsConfigurationSource
        http.cors(withDefaults());
    }

    // CORS configuration
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
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
