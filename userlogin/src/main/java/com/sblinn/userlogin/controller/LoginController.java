package com.sblinn.userlogin.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.sblinn.userlogin.dao.InvalidEmailException;
import com.sblinn.userlogin.dao.InvalidUsernameException;
import com.sblinn.userlogin.dto.Authority;
import com.sblinn.userlogin.dto.UserEntity;
import com.sblinn.userlogin.service.UserService;



@Controller
public class LoginController {
 
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired 
    UserService userService;


    @GetMapping("/login")
    public String showLoginForm() {
        
        return "login";
    }

    @PostMapping(value = {"/login", "/login/?error"})
    public String submitLogin(HttpServletRequest request, Model model) {
        
        return "admin";
    }

    @GetMapping("/newuser")
    public String showNewUserForm(HttpServletRequest request, Model model) {

        model.addAttribute("userEntity", new UserEntity());
        return "newuser";
    }

    @PostMapping("/newuser")
    public String submitNewUser(HttpServletRequest request,
        @Valid UserEntity userEntity, 
        BindingResult result, Model model) {
        
        String emailAddress = request.getParameter("emailAddress");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm-password");

        List<GrantedAuthority> authorities = new ArrayList<>();
        
        authorities.add(new Authority("ROLE_USER"));
        authorities.add(new Authority("ROLE_ADMIN"));

        UserEntity userDetails = new UserEntity();
            userDetails.setUsername(username);
            userDetails.setPassword(passwordEncoder.encode(password)); 
            userDetails.setEmailAddress(emailAddress);
            userDetails.setAuthorities(authorities);
            userDetails.setIsEnabled(true);

        // Allow UserDetailsServiceImpl to validate the user details and 
        // throw exceptions to prevent duplicate users by email or username.
        try {
            userService.createUser(userDetails);
        } catch (InvalidEmailException e) {
            // add field error to the BindingResult for userEntity
            result.addError(new FieldError("userEntity", 
                "emailAddress", e.getMessage()));
        } catch (InvalidUsernameException e) {
            result.addError(new FieldError("userEntity", 
                "username", e.getMessage()));
        }

        // Add global error to the confirm-password field, which has 'global', 
        // if input password values do not match.
        if (!confirmPassword.equals(password)) {
            final String errorMsg = "Passwords must match.";
            result.addError(new ObjectError("userEntity", errorMsg));
        }

        // Reload the same newuser page if any errors were found.
        if (result.hasErrors()) {
            model.addAttribute("userEntity", userEntity);
            return "newuser";
        } else {
            // Authenticate the new user and add to SecurityContext
            Authentication authentication 
                = new UsernamePasswordAuthenticationToken(userDetails, 
                    password, authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        return "login";
    }
    
    @GetMapping("/logout")
    public String logoutDo(HttpServletRequest request,
            HttpServletResponse response, Model model) {
        
        HttpSession session = request.getSession(false);
        SecurityContextHolder.clearContext();
        session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        for (Cookie cookie : request.getCookies()) {
            cookie.setMaxAge(0);
        }

        model.addAttribute("responseMsg", "Logout successful.");
        
        return "logout";
    }
    
}
