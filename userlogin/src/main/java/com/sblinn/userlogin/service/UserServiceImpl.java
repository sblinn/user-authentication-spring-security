package com.sblinn.userlogin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sblinn.userlogin.dao.InvalidEmailException;
import com.sblinn.userlogin.dao.InvalidUsernameException;
import com.sblinn.userlogin.dao.UserRepository;
import com.sblinn.userlogin.dto.UserEntity;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.getUserByUsername(username);
    }

    @Override
    public void createUser(UserEntity userEntity) throws InvalidEmailException, InvalidUsernameException {
        userRepository.createUser(userEntity);
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.getAllUsers();
    }
    
}
