package com.lm.bank.controller;

import com.lm.bank.busobj.dto.AccountDto;
import com.lm.bank.busobj.response.GeneralCreateResponse;
import com.lm.bank.busobj.response.GeneralResponse;
import com.lm.bank.busobj.response.GenericDataResponse;
import com.lm.bank.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/account")
public class AccountController {

    private static final Logger __ = LoggerFactory.getLogger(AccountController.class);

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(value = "/{id}")
    public GenericDataResponse<AccountDto> getAccount(@PathVariable(name = "id") Long id) {
        __.info("Getting account by id: {}", id);
        Optional<AccountDto> account = accountService.getAccount(id);
        GenericDataResponse<AccountDto> response;
        if (account.isPresent()) {
            response = GenericDataResponse.of(account.get());
            __.info("Account successfully got by id: {}", id);
        } else {
            String error = "Could not find account for given id: " + id;
            response = GenericDataResponse.of(error);
            __.info(error);
        }
        return response;
    }

    @GetMapping
    public GenericDataResponse<Page<AccountDto>> getAccounts(@RequestParam(name = "size", defaultValue = "100") int size, @RequestParam(name = "page", defaultValue = "0") int page) {
        __.info("getting accounts error current user");
        Page<AccountDto> accounts = accountService.getAccounts(new PageRequest(page, size));
        return GenericDataResponse.of(accounts);
    }

    @PostMapping
    public GeneralCreateResponse<Long> createAccount(@RequestParam(name = "amount", required = false) BigDecimal amount) {
        __.info("Received create account request");
        GeneralCreateResponse<Long> result = accountService.createAccount(amount);
        __.info("Create account request ended with result: {}", result.toString());
        return result;
    }

    @DeleteMapping
    public GeneralResponse deleteAccount(@RequestParam(name = "id") Long id) {
        __.info("Received remove account request");
        GeneralResponse result = accountService.deleteAccount(id);
        __.info("remove account request ended with result: {}", result.toString());
        return result;
    }

}
