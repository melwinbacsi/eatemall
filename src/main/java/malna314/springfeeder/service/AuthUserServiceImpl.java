package malna314.springfeeder.service;

import malna314.springfeeder.entity.AuthUser;
import malna314.springfeeder.repository.AuthUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthUserServiceImpl implements AuthUserService, UserDetailsService {

    private AuthUserRepository userRepository;

    @Autowired
    public void setUserRepository(AuthUserRepository userRepository){
        this.userRepository=userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser user = findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return new UserDetailsImpl(user);
    }

    @Override
    public AuthUser findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}