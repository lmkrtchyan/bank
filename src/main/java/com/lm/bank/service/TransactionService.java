package com.lm.bank.service;

import com.lm.bank.busobj.response.GeneralCreateResponse;
import com.lm.bank.busobj.response.TransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;

public interface TransactionService {
    Page<TransactionResponse> getTransaction(Long accountId, boolean isSent, PageRequest pageRequest);

    GeneralCreateResponse<Long> createTransaction(Long fromAccountId, Long toAccountId, BigDecimal amount, String description);

}
