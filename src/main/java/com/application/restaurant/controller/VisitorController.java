package com.application.restaurant.controller;

import com.application.restaurant.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class VisitorController {

    @GetMapping("/events")
    public String getAllUsers() {
        return "hi";

    }
}
