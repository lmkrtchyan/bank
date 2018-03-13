package com.lm.bank.service;

import com.lm.bank.busobj.dto.TransactionDto;
import com.lm.bank.busobj.response.GeneralCreateResponse;
import com.lm.bank.busobj.response.GeneralResponse;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface TransactionExecutorService {

    GeneralCreateResponse<Long> executeTransaction(TransactionDto transactionDto);
}
