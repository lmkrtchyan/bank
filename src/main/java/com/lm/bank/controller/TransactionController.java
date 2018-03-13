package com.lm.bank.controller;

import com.lm.bank.busobj.dto.AccountDto;
import com.lm.bank.busobj.dto.TransactionDto;
import com.lm.bank.busobj.response.GeneralCreateResponse;
import com.lm.bank.busobj.response.GeneralResponse;
import com.lm.bank.busobj.response.GenericDataResponse;
import com.lm.bank.busobj.response.TransactionResponse;
import com.lm.bank.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController("/transaction")
public class TransactionController {
    private static final Logger __ = LoggerFactory.getLogger(TransactionController.class);

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping(value = "/sent")
    public GenericDataResponse<Page<TransactionResponse>> getSentTransactions(@RequestParam("account_id") Long accountId,
                                                                     @RequestParam(name = "size", defaultValue = "100") int size,
                                                                     @RequestParam(name = "page", defaultValue = "0") int page) {
        __.info("Received getSentTransactions request for account:{}", accountId);
        Page<TransactionResponse> transactionDtoPage = transactionService.getTransaction(accountId, true, new PageRequest(page, size));
        __.info("getSentTransactions request ended successfully for account:{}", accountId);
        return GenericDataResponse.of(transactionDtoPage);
    }

    @PostMapping(value = "/sent")
    public GeneralCreateResponse<Long> createTransaction(@RequestParam("from_accountId") Long fromAccountId,
                                                         @RequestParam("to_accountId") Long toAccountId,
                                                         @RequestParam("amount") BigDecimal amount,
                                                         @RequestParam("description") String description){
        __.info("Received createTransactions request from account:\"{}\" to account:\"{}\"", fromAccountId, toAccountId);
        GeneralCreateResponse<Long> transaction = transactionService.createTransaction(fromAccountId, toAccountId, amount, description);
        __.info("createTransactions request from account:\"{}\" to account:\"{}\" ended with response: {}", fromAccountId, toAccountId, transaction.toString());
        return transaction;
    }

    @GetMapping(value = "/received")
    public GenericDataResponse<Page<TransactionResponse>> getReceivedTransactions(@RequestParam("account_id") Long accountId,
                                                                          @RequestParam(name = "size", defaultValue = "100") int size,
                                                                          @RequestParam(name = "page", defaultValue = "0") int page) {
        __.info("Received getReceivedTransactions request for account:{}", accountId);
        Page<TransactionResponse> transactionDtoPage = transactionService.getTransaction(accountId, false, new PageRequest(page, size));
        __.info("getSentTransactions request ended successfully for account:{}", accountId);
        return GenericDataResponse.of(transactionDtoPage);
    }

}
