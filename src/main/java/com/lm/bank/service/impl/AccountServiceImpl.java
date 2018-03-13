package com.lm.bank.service.impl;

import com.lm.bank.busobj.SessionDataContainer;
import com.lm.bank.busobj.dto.AccountDto;
import com.lm.bank.busobj.dto.UserDto;
import com.lm.bank.busobj.response.GeneralCreateResponse;
import com.lm.bank.busobj.response.GeneralResponse;
import com.lm.bank.dao.AccountDao;
import com.lm.bank.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger __ = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final AccountDao accountDao;

    private final SessionDataContainer sessionDataContainer;

    @Autowired
    public AccountServiceImpl(AccountDao accountDao, SessionDataContainer sessionDataContainer) {
        this.accountDao = accountDao;
        this.sessionDataContainer = sessionDataContainer;
    }

    @Override
    public Optional<AccountDto> getAccount(Long id) {
        UserDto userDto = sessionDataContainer.getUserDto();
        return accountDao.findByIdAndOwner(id, userDto);
    }

    @Override
    public Page<AccountDto> getAccounts(Pageable pageable) {
        UserDto userDto = sessionDataContainer.getUserDto();
        return accountDao.findByOwner(userDto, pageable);
    }

    @Override
    public GeneralCreateResponse<Long> createAccount(BigDecimal amount) {
        try {
            AccountDto accountDto = new AccountDto();
            UserDto userDto = sessionDataContainer.getUserDto();
            accountDto.setOwner(userDto);
            accountDto.setAmount(amount);
            accountDto = accountDao.save(accountDto);
            Set<AccountDto> accounts = userDto.getAccounts();
            if (accounts == null) {
                accounts = new HashSet<>();
                userDto.setAccounts(accounts);
            }
            accounts.add(accountDto);
            return GeneralCreateResponse.of(accountDto.getId());
        } catch (Exception e) {
            __.error("Error saving account", e);
            return GeneralCreateResponse.error("Error saving account: " + e.getMessage());
        }
    }

    @Override
    public GeneralResponse deleteAccount(Long id) {
        UserDto userDto = sessionDataContainer.getUserDto();
        Integer result = accountDao.deleteByIdAndOwner(id, userDto);
        userDto.getAccounts().removeIf(accountDto -> accountDto.getId().equals(id));
        return new GeneralResponse(result > 0);
    }
}
