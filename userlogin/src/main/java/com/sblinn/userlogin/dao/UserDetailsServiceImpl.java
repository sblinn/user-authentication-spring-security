
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

    
    public UserDetailsServiceImpl() {
        super();
        final String DEF_INSERT_AUTHORITY_SQL 
            = "INSERT INTO User_Authorities (username, authority) VALUES (?,?)";
        super.setCreateAuthoritySql(DEF_INSERT_AUTHORITY_SQL);
        
        final String DEF_DELETE_USER_AUTHORITIES_SQL
            = "DELETE FROM User_Authorities WHERE username = ?";
        super.setDeleteUserAuthoritiesSql(DEF_DELETE_USER_AUTHORITIES_SQL);
    }
    
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
