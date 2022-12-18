package com.sblinn.userlogin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.sblinn.userlogin.dto.UserEntity;
import com.sblinn.userlogin.service.UserService;

@Controller
public class AdminController {
    
    @Autowired
    private UserService userService;

    @GetMapping("/admin")
    public String displayAdminPage(Model model) {
        
        List<UserEntity> userEntities = userService.getAllUsers();
        model.addAttribute("userEntities", userEntities);

        return "admin";
    }
    
}