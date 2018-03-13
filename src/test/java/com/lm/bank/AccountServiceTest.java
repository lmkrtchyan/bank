package com.lm.bank;

import com.lm.bank.busobj.dto.AccountDto;
import com.lm.bank.busobj.dto.UserDto;
import com.lm.bank.busobj.response.GeneralCreateResponse;
import com.lm.bank.busobj.response.GeneralResponse;
import com.lm.bank.service.AccountService;
import com.lm.bank.service.AuthService;
import com.lm.bank.service.UserService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private TestCommons testCommons;

    @Before
    public void init() {
        testCommons.createTestUser();
        GeneralResponse login = authService.login(TestCommons.EMAIL, TestCommons.PASSWORD);
        Assert.assertTrue("Login failed with message:" + login.getErrorMessage(), login.isSuccess());
    }

    @After
    public void destroy() {
        testCommons.removeTestUser();
    }

    @Test
    @Transactional
    public void accountServiceTest() {
        BigDecimal amount = new BigDecimal(1000);
        GeneralCreateResponse<Long> accountResponse = accountService.createAccount(amount);
        Assert.assertTrue("Account was not created with message: " + accountResponse.getErrorMessage(), accountResponse.isSuccess());
        Optional<AccountDto> account = accountService.getAccount(accountResponse.getId());
        Assert.assertTrue("Acount was not created", account.isPresent());
        Assert.assertEquals("Account amount is incorrect", amount, account.get().getAmount());
        UserDto currentUser = userService.getCurrentUser();
        Assert.assertEquals("Owner of account is incorrect", currentUser, account.get().getOwner());
        GeneralResponse response = accountService.deleteAccount(accountResponse.getId());
        Assert.assertTrue("Account wasn't deleted with message:" + response.getErrorMessage(), response.isSuccess());
    }
}
