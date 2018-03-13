package com.lm.bank;

import com.lm.bank.busobj.response.GeneralResponse;
import com.lm.bank.service.AuthService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class AuthServiceTests {

    @Autowired
    private TestCommons testCommons;

    @Autowired
    private AuthService authService;

    @Before
    public void init() {
        testCommons.createTestUser();
    }

    @After
    public void destroy() {
        testCommons.removeTestUser();
    }

    @Test
    public void loginTest() {
        GeneralResponse login = authService.login(TestCommons.EMAIL, TestCommons.PASSWORD);
        Assert.assertTrue("Login failed with message:" + login.getErrorMessage(), login.isSuccess());
    }

    @Test
    public void failedLoginTest() {
        GeneralResponse login = authService.login(TestCommons.EMAIL, TestCommons.PASSWORD + "--");
        Assert.assertFalse("Login must be failed but succeed", login.isSuccess());
    }


}
