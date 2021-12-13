package com.application.restaurant.login;

import com.application.restaurant.dao.UserRepository;
import com.application.restaurant.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping
public class LoginController {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "page-login";
    }

    @RequestMapping("/waiterDashboard")
    public ModelAndView waiterDashboard() {
        return new ModelAndView("waiterdashboard");
    }

    @RequestMapping("/adminDashboard")
    public ModelAndView adminDashboard() {
        return new ModelAndView("admindashboard");
    }
}
