

package com.sblinn.userlogin.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.security.core.GrantedAuthority;

/**
 * Authority is an Entity DTO implementation of Spring Security's 
 * GrantedAuthority interface.
 */
@Entity
@Table(name = "Authorities")
public class Authority implements GrantedAuthority {

    @Id
    @Column(length = 25)
    private String authority;

    
    public Authority() {
        
    }
    
    public Authority(String authority) {
        this.authority = authority;
    }
    
    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
    
}
