package com.lm.bank.dao;

import com.lm.bank.busobj.dto.UserDto;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


public interface UserDao extends Repository<UserDto, Long> {
    Optional<UserDto> getUserDtoByEmailAndPassword(String email, String password);

    UserDto save(UserDto userDao);

    @Transactional
    @Modifying
    Integer deleteById(Long id);
}
