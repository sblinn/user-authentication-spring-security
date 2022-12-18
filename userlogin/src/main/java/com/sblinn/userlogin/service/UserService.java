package com.sblinn.userlogin.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.sblinn.userlogin.dao.InvalidEmailException;
import com.sblinn.userlogin.dao.InvalidUsernameException;
import com.sblinn.userlogin.dto.UserEntity;

public interface UserService extends UserDetailsService {
    
    //UserEntity loadUserByUsername(String username);

    void createUser(UserEntity userEntity) throws 
        InvalidEmailException, 
        InvalidUsernameException;

    List<UserEntity> getAllUsers();

}
