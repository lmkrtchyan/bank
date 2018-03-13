package com.lm.bank.service.impl;

import com.lm.bank.busobj.SessionDataContainer;
import com.lm.bank.busobj.dto.UserDto;
import com.lm.bank.busobj.response.GeneralResponse;
import com.lm.bank.dao.UserDao;
import com.lm.bank.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserDao userDao;

    private final SessionDataContainer sessionDataContainer;

    @Autowired
    public AuthServiceImpl(UserDao userDao, SessionDataContainer sessionDataContainer) {
        this.userDao = userDao;
        this.sessionDataContainer = sessionDataContainer;
    }

    @Override
    public GeneralResponse login(String email, String password) {
        Optional<UserDto> userOp = userDao.getUserDtoByEmailAndPassword(email, password);
        userOp.ifPresent(sessionDataContainer::setUserDto);
        return new GeneralResponse(userOp.isPresent());
    }

    @Override
    public GeneralResponse logout() {
        GeneralResponse response = new GeneralResponse(isAuthenticated());
        sessionDataContainer.setUserDto(null);
        return response;
    }

    @Override
    public Boolean isAuthenticated() {
        return sessionDataContainer.getUserDto() != null;
    }
}
