package com.lm.bank;

import com.lm.bank.busobj.dto.AccountDto;
import com.lm.bank.busobj.response.GeneralCreateResponse;
import com.lm.bank.busobj.response.GeneralResponse;
import com.lm.bank.dao.TransactionDao;
import com.lm.bank.service.AccountService;
import com.lm.bank.service.AuthService;
import com.lm.bank.service.TransactionService;
import com.lm.bank.service.UserService;
import com.lm.bank.util.DaoUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import static com.lm.bank.TestCommons.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BankApplicationTests {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    WebApplicationContext wac;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionDao transactionDao;

    @Autowired
    private TestCommons testCommons;

    @Autowired
    private DaoUtil daoUtil;

    private static final Logger __ = LoggerFactory.getLogger(BankApplicationTests.class);

    public Long[] createTestUserAndAccounts() {
        testCommons.createTestUser();
        GeneralResponse login = authService.login(EMAIL, PASSWORD);
        Assert.assertTrue("login failed with message: " + login.getErrorMessage(), login.isSuccess());
        GeneralCreateResponse<Long> account = accountService.createAccount(new BigDecimal(1000));
        Long[] accountIds = new Long[2];
        Assert.assertTrue("account not created with message: " + account.getErrorMessage(), account.isSuccess());
        accountIds[0] = account.getId();
        account = accountService.createAccount(new BigDecimal(100));
        Assert.assertTrue("account not created with message: " + account.getErrorMessage(), account.isSuccess());
        accountIds[1] = account.getId();
        return accountIds;
    }

    @Test
    public void stressTest() throws Exception {
        List<Long> transactionIds = new ArrayList<>();
        try {
            Long[] ids = daoUtil.executeInTransaction(this::createTestUserAndAccounts);

            ExecutorService executorService = Executors.newFixedThreadPool(7);
            CountDownLatch countDownLatch = new CountDownLatch(7);
            List<Future<GeneralCreateResponse<Long>>> tasks = new ArrayList<>();
            //Run 7 threads with transactions on same accounts
            tasks.add(createTransaction(executorService, countDownLatch, ids[0], ids[1], 50));
            tasks.add(createTransaction(executorService, countDownLatch, ids[0], ids[1], 75));
            tasks.add(createTransaction(executorService, countDownLatch, ids[0], ids[1], 120));
            tasks.add(createTransaction(executorService, countDownLatch, ids[0], ids[1], 150));
            tasks.add(createTransaction(executorService, countDownLatch, ids[0], ids[1], 50));
            tasks.add(createTransaction(executorService, countDownLatch, ids[0], ids[1], 250));
            tasks.add(createTransaction(executorService, countDownLatch, ids[1], ids[0], 30));
            executorService.shutdown();
            Assert.assertTrue("executorService wasn't turned error in 20 seconds", executorService.awaitTermination(20, TimeUnit.SECONDS));

            for (int i = 0; i < tasks.size(); i++) {
                GeneralCreateResponse<Long> createResponse = tasks.get(i).get();
                Assert.assertTrue("task " + i + " is not completed properly: " + createResponse.getErrorMessage(), createResponse.isSuccess());
                if (createResponse.isSuccess()) {
                    transactionIds.add(createResponse.getId());
                }
            }
            Optional<AccountDto> accountOpt = accountService.getAccount(ids[0]);
            Assert.assertTrue("Account is not present in db", accountOpt.isPresent());
            Assert.assertEquals("Not all Transactions processed correctly", new BigDecimal(335.00).intValue(), accountOpt.get().getAmount().intValue());
        } finally {
            daoUtil.executeInTransaction(() -> {
                testCommons.removeTestUser();
                transactionDao.deleteByIdIn(transactionIds);
            });
        }
    }

    private Future<GeneralCreateResponse<Long>> createTransaction(ExecutorService executorService, CountDownLatch countDownLatch,
                                                      Long fromAccount, Long toAccount, Integer amount) {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        return executorService.submit(() -> {
            GeneralCreateResponse<Long> response;
            RequestContextHolder.setRequestAttributes(requestAttributes);
            countDownLatch.countDown();
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
               __.error("error waiting countDownLatch", e);
               throw e;
            }
            try {
                response = transactionService.createTransaction(fromAccount, toAccount, new BigDecimal(amount), "desc");
            } catch (Exception e) {
                __.error("", e);
                throw e;
            }
            return response;
        });
    }

}
