package com.sblinn.userlogin.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;


/**
 * UserEntity is an Entity DTO implementation of UserDetails 
 * for user data, including the secure login information used 
 * by Spring Security User class as well as any other data that 
 * is not handled by the User class. 
 */
@Entity
@Table(name = "Users")
public class UserEntity implements UserDetails {
    
    @javax.persistence.Id
    @Column(length = 50, nullable = false, unique = true)
    @NotBlank(message = "Username required.")
    @Size(max = 50, message = "Username must be less than 50 characters.")
    private String username;

    @Column(length = 100, nullable = false, unique = false)
    @NotBlank(message = "User password required.")
    @Size(max = 100, message = "Password must be less than 50 characters.")
    private String password;

    @Column(length = 254, nullable = false, unique = true)
    @NotBlank(message = "Email address required.")
    @Size(max = 254, message = "Email address must be less than 255 characters.")
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String emailAddress;
    
    @Column(name = "enabled", nullable = false)
    private boolean isEnabled;
    
    @ManyToMany(fetch = FetchType.EAGER, targetEntity=Authority.class)
    @JoinTable(name = "user_authorities",
            joinColumns = @JoinColumn(
                    name = "username", 
                    referencedColumnName = "username"),
            inverseJoinColumns = @JoinColumn(
                    name = "authority", 
                    referencedColumnName = "authority"))
    private Collection<? extends GrantedAuthority> authorities;


    /**
     * Constructs a new empty User -- used for communication between view and controller.
     */
    public UserEntity() {
        
    }

    /**
     * Constructs a new User, sets default non-locked account and credentials.
     * @param username
     * @param password
     * @param emailAddress
     * @param isEnabled
     * @param authorities
     */
    public UserEntity(String username, String password, String emailAddress, 
            boolean isEnabled, Collection<? extends GrantedAuthority> authorities) {

        this.username = username;
        this.password = password;
        this.emailAddress = emailAddress;
        this.isEnabled = isEnabled;
        setAuthorities(authorities);
    }
    

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Collection<GrantedAuthority> getAuthorities() {
        return Collections.unmodifiableCollection(this.authorities);
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        // The following block comes from Spring Security's User Class:
        Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
		// Ensure array iteration order is predictable (as per
		// UserDetails.getAuthorities() contract and SEC-717)
		Collection<GrantedAuthority> sortedAuthorities = new ArrayList<>();
		for (GrantedAuthority grantedAuthority : authorities) {
			Assert.notNull(grantedAuthority, "GrantedAuthority list cannot contain any null elements");
			sortedAuthorities.add(grantedAuthority);
		}

        this.authorities = Collections.unmodifiableCollection(sortedAuthorities);
    }

    /*
     * isAccountNonExpired, isCredentialsNonExpired and isAccountNonLocked 
     * will return true as there is currently no need for account or 
     * credentials expiration nor account locking in this version. 
     */

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    
}
