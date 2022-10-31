package com.sblinn.userlogin.dto;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "Users")
public class User {
    
    @javax.persistence.Id
    @Column(length = 50, nullable = false, unique = true)
    @NotBlank(message = "Username required.")
    @Size(max = 50, message = "Username must be less than 50 characters.")
    private String username;

    @Column(length = 100, nullable = false, unique = false)
    @NotBlank(message = "User password required.")
    @Size(max = 100, message = "Password must be less than 50 characters.")
    private String password;
    
    @Column(name = "enabled", nullable = false)
    private boolean isEnabled;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_authorities",
            joinColumns = @JoinColumn(
                    name = "username", 
                    referencedColumnName = "username"),
            inverseJoinColumns = @JoinColumn(
                    name = "authority", 
                    referencedColumnName = "authority"))
    private Collection<Authority> authorities;

    
    public User() {
        this.authorities = new ArrayList<>();
    }
    
    public User(String username, String password, boolean isEnabled) {
        this.username = username;
        this.password = password;
        this.isEnabled = isEnabled;
        this.authorities = new ArrayList<>();
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

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Collection<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<Authority> authorities) {
        this.authorities = authorities;
    }
    
    
}