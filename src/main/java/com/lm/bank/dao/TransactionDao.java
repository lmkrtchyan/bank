package com.lm.bank.dao;

import com.lm.bank.busobj.dto.AccountDto;
import com.lm.bank.busobj.dto.TransactionDto;
import com.lm.bank.busobj.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

public interface TransactionDao extends Repository<TransactionDto, Long> {

    Page<TransactionDto> getTransactionDtoByToAccountId(Long toAccountId, Pageable pageable);

    Page<TransactionDto> getTransactionDtoByFromAccountId(Long fromAccountId, Pageable pageable);

    TransactionDto save(TransactionDto transactionDto);

    @Transactional
    @Modifying
    Integer deleteByIdIn(Iterable<? extends Long> iterable);
}
