package com.application.restaurant.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/homepage")
public class HomePageController {

    @RequestMapping("/")
    public String homePage() {
        return "homepage";
    }
}
