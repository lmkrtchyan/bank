package com.lm.bank.controller;

import com.lm.bank.busobj.LoginNotRequired;
import com.lm.bank.busobj.response.GeneralResponse;
import com.lm.bank.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth")
@LoginNotRequired
public class AuthController {

    private static final Logger __ = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping
    public GeneralResponse login(@RequestParam(name = "email") String email, @RequestParam(name = "password") String password) {
        __.info("Login request for user \"{}\"", email);
        GeneralResponse response = authService.login(email, password);
        __.info("Login request for user \"{}\" ended with response: {}", email, response.toString());
        return response;
    }

    @DeleteMapping
    public GeneralResponse logout() {
        __.info("Logout request");
        GeneralResponse response = authService.logout();
        __.info("Logout request ended with response: {}", response.toString());
        return response;
    }

    @GetMapping
    public Boolean isAuthenticated() {
        __.info("isAuthenticated request");
        Boolean response = authService.isAuthenticated();
        __.info("isAuthenticated request ended with response: {}", response.toString());
        return response;
    }

}
