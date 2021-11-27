package com.application.restaurant.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class HomePageController {

    @RequestMapping("/homepage")
    public String homePage() {
        return "homepage";
    }
}
