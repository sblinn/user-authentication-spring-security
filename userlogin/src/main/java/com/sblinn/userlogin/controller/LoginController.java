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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.sblinn.userlogin.dao.UserDetailsServiceImpl;
import com.sblinn.userlogin.dto.Authority;
import org.springframework.security.core.GrantedAuthority;



@Controller
public class LoginController {
 
    @Autowired
    PasswordEncoder passwordEncoder;


    @Autowired 
    UserDetailsServiceImpl userDetailsServiceImpl;


    @GetMapping("/login")
    public String showLoginForm() {
        
        return "login";
    }

    @PostMapping(value = {"/login", "/login/?error"})
    public String submitLogin(HttpServletRequest request, Model model) {
        
        return "admin";
    }

    @GetMapping("/newuser")
    public String showNewUserForm(Model model) {
        model.addAttribute("user", 
        new com.sblinn.userlogin.dto.User());

        return "newuser";
    }

    @PostMapping("/newuser")
    public String submitNewUser(HttpServletRequest request,
        @Valid com.sblinn.userlogin.dto.User user, 
        BindingResult result, Model model) {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm-password");

        if (!confirmPassword.equals(password)) {
            String errorMsg = "Passwords must match.";
            ObjectError error 
                = new ObjectError("globalError", errorMsg);
            result.addError(error);
            model.addAttribute("username", username);

            return "newuser";
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        
        authorities.add(new Authority("ROLE_USER"));
        //authorities.add(new Authority("ROLE_ADMIN"));

        UserDetails userDetails 
                = new org.springframework.security.core.userdetails.User(
                    username, passwordEncoder.encode(password), authorities);

        userDetailsServiceImpl.createUser(userDetails);
        Authentication authentication 
                = new UsernamePasswordAuthenticationToken(userDetails, 
                    password, authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);

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
