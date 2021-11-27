package com.application.restaurant.login;

import com.application.restaurant.model.User;
import com.application.restaurant.registration.RegistrationRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping
public class LoginController {

    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
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
