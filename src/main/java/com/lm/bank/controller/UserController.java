package com.lm.bank.controller;

import com.lm.bank.busobj.LoginNotRequired;
import com.lm.bank.busobj.dto.UserDto;
import com.lm.bank.busobj.response.GeneralCreateResponse;
import com.lm.bank.busobj.response.GeneralResponse;
import com.lm.bank.busobj.response.GenericDataResponse;
import com.lm.bank.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger __ = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @LoginNotRequired
    public GeneralCreateResponse<Long> signUp(@RequestParam(name = "email") String email, @RequestParam(name = "password") String password) {
        __.info("SignUp request received with email: \"{}\"", email);
        GeneralCreateResponse<Long> generalResponse = userService.signUp(email, password);
        __.info("SignUp request with email: \"{}\" responses: {}", email, generalResponse.toString());
        return generalResponse;
    }

    @GetMapping
    public GenericDataResponse<UserDto> getCurrentUser() {
        __.info("get User request received");
        UserDto currentUser = userService.getCurrentUser();
        return GenericDataResponse.of(currentUser);
    }

    @DeleteMapping
    public GeneralResponse deleteUser() {
        __.info("received delete current user request");
        boolean success = userService.deleteUser();
        return new GeneralResponse(success, success ? null : "Error deleting user");
    }

}
