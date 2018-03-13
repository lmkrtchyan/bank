package com.lm.bank;

import com.lm.bank.busobj.response.GeneralCreateResponse;
import com.lm.bank.busobj.response.GeneralResponse;
import com.lm.bank.service.AuthService;
import com.lm.bank.service.UserService;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestCommons {
    static final String EMAIL = "test@test.com";
    static final String PASSWORD = "pass";

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    public GeneralCreateResponse<Long> createTestUser() {
        GeneralCreateResponse<Long> response = userService.signUp(EMAIL, PASSWORD);
        Assert.assertTrue("User not created with message: " + response.getErrorMessage(), response.isSuccess());
        return response;
    }

    public void removeTestUser() {
        GeneralResponse login = authService.login(EMAIL, PASSWORD);
        Assert.assertTrue("Login failed with message:" + login.getErrorMessage(), login.isSuccess());
        boolean b = userService.deleteUser();
        Assert.assertTrue("User was not deleted ", b);
    }
}
