package com.sblinn.userlogin;

import com.sblinn.userlogin.dao.UserDetailsServiceImpl;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    
    @Autowired
    private DataSource dataSource;

    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) 
            throws Exception {

        http.authenticationProvider(authenticationProvider())
                .authorizeRequests()
                .antMatchers("/content/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                .antMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers("/", "/home", "/login", "/newuser").permitAll()
                .antMatchers("/css/**", "/js/**", "/fonts/**").permitAll()
                
                .anyRequest().authenticated()
            .and()
            
            .formLogin()
                .loginPage("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .failureUrl("/login?error")
                // this is here for debugging purposes, can be removed from production
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, 
                            HttpServletResponse response,
                            org.springframework.security.core.AuthenticationException exception) 
                            throws IOException, ServletException {
                        
                        String username = request.getParameter("username");
                        String password = request.getParameter("password");
                        String error = exception.getMessage();
                        
                        System.out.println("Login attempt failed for: "
                                + username + " : " + password 
                                + ". Reason: " + error);

                        String redirectUrl = request.getContextPath() + "/login?error";
                        response.sendRedirect(redirectUrl);
                    }
                })
                .permitAll()
            .and()
            .logout()
                .logoutSuccessUrl("/logout")
                .deleteCookies("JSESSIONID")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .permitAll() 
            .and()
                .exceptionHandling()
            .and()
                .headers()
		.frameOptions().sameOrigin();
            
        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean 
    UserDetailsServiceImpl userDetailsServiceImpl() {
        UserDetailsServiceImpl uds = new UserDetailsServiceImpl();
        uds.setDataSource(dataSource);
        return uds;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetailsServiceImpl uds = new UserDetailsServiceImpl();
        uds.setDataSource(dataSource);
        
        return uds;
    } 

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) 
                throws Exception {

        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        
        DaoAuthenticationProvider authProvider =
                new DaoAuthenticationProvider();
        
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
    
        return authProvider;
    }

}
    

