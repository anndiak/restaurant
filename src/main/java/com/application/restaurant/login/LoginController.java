package com.application.restaurant.login;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/login")
@AllArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @PostMapping
    public String login(@RequestBody LoginRequest loginRequest) {
        return loginService.login(loginRequest.getEmail(), loginRequest.getPassword());
    }

    @PostMapping("/asAdmin")
    public String loginAsAdmin(@RequestBody LoginRequest loginRequest) {
        return loginService.login(loginRequest.getEmail(), loginRequest.getPassword());
    }


}
