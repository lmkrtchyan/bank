package com.lm.bank.service.impl;

import com.lm.bank.busobj.dto.TransactionDto;
import com.lm.bank.busobj.response.GeneralCreateResponse;
import com.lm.bank.busobj.response.GeneralResponse;
import com.lm.bank.busobj.response.TransactionResponse;
import com.lm.bank.dao.TransactionDao;
import com.lm.bank.service.TransactionExecutorService;
import com.lm.bank.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Value("${transaction.retry.count}")
    private int retryCount;

    private final TransactionDao transactionDao;

    private final TransactionExecutorService transactionExecutorService;

    private static final Logger __ = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    public TransactionServiceImpl(TransactionDao transactionDao, TransactionExecutorService transactionExecutorService) {
        this.transactionDao = transactionDao;
        this.transactionExecutorService = transactionExecutorService;
    }

    @Override
    @Transactional
    public Page<TransactionResponse> getTransaction(Long accountId, boolean isSent, PageRequest pageRequest) {
        Page<TransactionDto> transactionDtos;
        if (isSent) {
             transactionDtos = transactionDao.getTransactionDtoByFromAccountId(accountId, pageRequest);
        } else {
            transactionDtos = transactionDao.getTransactionDtoByToAccountId(accountId, pageRequest);
        }
        return transactionDtos.map((t) -> new TransactionResponse(t, isSent));
    }

    @Override
    public GeneralCreateResponse<Long> createTransaction(Long fromAccountId, Long toAccountId, BigDecimal amount, String description) {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setAmount(amount);
        transactionDto.setDescription(description);
        transactionDto.setFromAccountId(fromAccountId);
        transactionDto.setToAccountId(toAccountId);
        GeneralCreateResponse<Long> response = null;
        String error = null;
        for (int i = 0; i < retryCount; i++) {
            try{
                response = transactionExecutorService.executeTransaction(transactionDto);
                break;
            } catch (Exception e) {
                error = e.getMessage();
                __.error("", e);
            }
        }
        if(response == null) {
            response = GeneralCreateResponse.error(error);
        }
        return response;
    }
}
