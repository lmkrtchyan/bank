package com.lm.bank.service;

import com.lm.bank.busobj.dto.AccountDto;
import com.lm.bank.busobj.response.GeneralCreateResponse;
import com.lm.bank.busobj.response.GeneralResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;

public interface AccountService {
    Optional<AccountDto> getAccount(Long id);

    Page<AccountDto> getAccounts(Pageable pageable);

    GeneralCreateResponse<Long> createAccount(BigDecimal amount);

    GeneralResponse deleteAccount(Long id);
}
