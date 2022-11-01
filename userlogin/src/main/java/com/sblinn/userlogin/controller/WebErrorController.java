package com.sblinn.userlogin.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class WebErrorController implements ErrorController {
    
    @RequestMapping("/error")
    @ResponseBody 
    public ModelAndView error(HttpServletResponse response, 
            Model model) {

        // handle 403 Status Code : Forbidden
        /* Spring AccessDeniedHandler already handles AccessDeniedException 
            by returning 403 status -- which could be overriden if needed -- 
            for now we can just inform the user. */ 
        if (response.getStatus() == 403) {
           final String errorMsg = "Access Denied: Insufficient Authority. ";
            model.addAttribute("responseMsg", errorMsg);
        }
        
        ModelAndView modelAndView = new ModelAndView(getErrorPath());
        
        return modelAndView;
    }

    public String getErrorPath() {
        return "/error";
    }

}
