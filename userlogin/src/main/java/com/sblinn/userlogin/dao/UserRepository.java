package com.sblinn.userlogin.dao;

import java.util.List;

import org.springframework.data.repository.Repository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.sblinn.userlogin.dto.UserEntity;

/**
 * Repository interface for UserEntity.
 */
public interface UserRepository extends Repository<UserEntity, String> {
    
    void createUser(UserEntity userEntity) throws 
        InvalidEmailException, 
        InvalidUsernameException;

    boolean userExists(String username);

    UserEntity getUserByUsername(String username) 
            throws UsernameNotFoundException;

    List<UserEntity> getAllUsers();

}
