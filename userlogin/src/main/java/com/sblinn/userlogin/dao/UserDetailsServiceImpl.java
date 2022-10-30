
package com.sblinn.userlogin.dao;

import com.sblinn.userlogin.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.JdbcUserDetailsManager;


public class UserDetailsServiceImpl extends JdbcUserDetailsManager implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    
    @Override
    public UserDetails loadUserByUsername(String username) 
            throws UsernameNotFoundException {

        User user = userRepository.findUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User does not exist.");
        }
         
        return new UserDetailsImpl(user);
    }

}
