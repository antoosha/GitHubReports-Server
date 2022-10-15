package cz.cvut.fit.sp1.githubreports.security;

import cz.cvut.fit.sp1.githubreports.service.user.user.UserSPI;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsServices implements UserDetailsService {

    private final UserSPI userService;

    public AppUserDetailsServices(@Qualifier("UserService") UserSPI userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new AppUserDetails(userService.readByUsername(username));
    }
}
