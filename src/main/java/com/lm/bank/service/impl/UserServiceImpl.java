package com.lm.bank.service.impl;

import com.lm.bank.busobj.SessionDataContainer;
import com.lm.bank.busobj.dto.UserDto;
import com.lm.bank.busobj.response.GeneralCreateResponse;
import com.lm.bank.dao.UserDao;
import com.lm.bank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Autowired
    private UserDao userDao;

    @Autowired
    private SessionDataContainer sessionDataContainer;

    @Override
    public GeneralCreateResponse<Long> signUp(String email, String password) {
        GeneralCreateResponse<Long> response;
        if (!VALID_EMAIL_ADDRESS_REGEX .matcher(email).find()) {
            response = GeneralCreateResponse.error("Email address is incorrect");
        } else {
            try {
                UserDto userDto = userDao.save(new UserDto(email, password));
                response = GeneralCreateResponse.of(userDto.getId());
            } catch (Exception e) {
                response = GeneralCreateResponse.error(e.getMessage());
            }
        }
        return response;
    }

    @Override
    public UserDto getCurrentUser() {
        return sessionDataContainer.getUserDto();
    }

    @Override
    public boolean deleteUser() {
        Integer delete = userDao.deleteById(sessionDataContainer.getUserDto().getId());
        return delete != null && delete > 0;
    }
}
