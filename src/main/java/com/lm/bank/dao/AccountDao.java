package com.lm.bank.dao;

import com.lm.bank.busobj.dto.AccountDto;
import com.lm.bank.busobj.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface AccountDao extends Repository<AccountDto, Long> {

    Optional<AccountDto> findByIdAndOwner(Long id, UserDto owner);

    Optional<AccountDto> findById(Long id);

    Page<AccountDto> findByOwner(UserDto owner, Pageable pageable);

    AccountDto save(AccountDto accountDto);

    @Transactional
    @Modifying
    Integer deleteByIdAndOwner(Long id, UserDto owner);

}
