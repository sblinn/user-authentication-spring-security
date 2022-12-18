
package com.sblinn.userlogin.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import com.sblinn.userlogin.dto.UserEntity;

/**
 * 
 */
@Repository
public class UserRepositoryImpl implements UserRepository {

    // see JdbcUserDetailsManager class 

    @Autowired
    private SessionFactory sessionFactory;


    public UserRepositoryImpl() {
        
    }
    

	public void createUser(final UserEntity user) throws 
            InvalidEmailException, 
            InvalidUsernameException {
        
        validateUser(user);

        Session session = null;

        try {
            buildUserForAuthentication(user, user.getAuthorities());
            session = sessionFactory.openSession();
            session.beginTransaction();
            String username = (String) session.save(user);
            System.out.println("USER CREATED: " + username);
            session.getTransaction().commit();
            
        } catch (Exception e) {
            System.out.println("-------ERROR CAUSED BY: " + e.getMessage());
        }

        session.close();
	}

    @Override
    public boolean userExists(String username) {
        try {
            getUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return true;
        }
        return false;
    }

    /*
     * Case-insensitive query for user by username.
     */
    @Override
    public UserEntity getUserByUsername(String username) 
            throws UsernameNotFoundException {

        UserEntity user = null;
        Session session = null;
        
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            user = session.createQuery(
                "SELECT u FROM UserEntity u WHERE u.username = :username", 
                    UserEntity.class)
                    .setParameter("username", username)
                    .getSingleResult();
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("-------ERROR CAUSED BY: " + e.getMessage());
        }

        session.close();
         
        if (user == null) {
            throw new UsernameNotFoundException("User does not exist.");
        }
         
        return user;
    }

    public List<UserEntity> getAllUsers() {
        List<UserEntity> users = new ArrayList<UserEntity>();
        Session session = null;
        
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            users = session.createQuery(
                    "SELECT u FROM UserEntity u ORDER BY u.username", 
                    UserEntity.class).list();
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("-------ERROR CAUSED BY: " + e.getMessage());
        }

        session.close();
         
        return users;
    }

    private List<String> getAllUsernames() {
        List<String> usernames = new ArrayList<>();
        Session session = null;
        
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            usernames = session.createQuery(
                "SELECT username FROM UserEntity u ORDER BY u.username", 
                    String.class).list();
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("-------ERROR CAUSED BY: " + e.getMessage());
        }

        session.close();

        return usernames;
    }

    private List<String> getAllUserEmailAddresses() {
        List<String> emailAddresses = new ArrayList<>();
        Session session = null;
        
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            emailAddresses = session.createQuery(
                "SELECT emailAddress FROM UserEntity u ORDER BY u.emailAddress", 
                    String.class).list();
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("-------ERROR CAUSED BY: " + e.getMessage());
        }

        session.close();
        
        return emailAddresses;
    }

    // private void insertUserAuthorities(UserDetails user) {
	// 	for (GrantedAuthority auth : user.getAuthorities()) {
	// 		getJdbcTemplate().update(DEF_INSERT_AUTHORITY_SQL, user.getUsername(), auth.getAuthority());
	// 	}
	// }


    /**
     * Create Spring Security User which then allows the
     * User class to perform validation, authentication and logging on the secure
     * data portion of the User entity (which contains all of the data,
     * including the data that User doesn't handle).
     * @param user
     * @param authorities
     * @return User
     */
    private User buildUserForAuthentication(UserEntity user, 
            Collection<? extends GrantedAuthority> authorities) {

        return new User(user.getUsername(), user.getPassword(), true, 
                true, true, 
                true, user.getAuthorities());
    }

    /**
     * Validates a UserEntity by checking that all required fields are filled 
     * and that username and email address are unique.
     * @param user
     * @throws InvalidEmailException
     */
    private void validateUser(UserEntity user) throws 
            InvalidEmailException,
            InvalidUsernameException {
                
        // all required fields 
        if (user.getUsername() == null ||
            user.getEmailAddress() == null ||
            user.getPassword() == null ||
            user.getAuthorities().isEmpty()) {

                throw new InvalidEmailException("Required field cannot be empty.");
        }

        // username and email address must be unique

        if (getAllUserEmailAddresses().contains(user.getEmailAddress())) {
            throw new InvalidEmailException("Email address is already in use.");
        }

        if (getAllUsernames().contains(user.getUsername())) {
            throw new InvalidUsernameException("Username is already in use.");
        }
    }

    

   
}
