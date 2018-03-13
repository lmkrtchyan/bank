package com.lm.bank.service;


import com.lm.bank.busobj.dto.UserDto;
import com.lm.bank.busobj.response.GeneralCreateResponse;
import com.lm.bank.busobj.response.GeneralResponse;

public interface UserService {
    GeneralCreateResponse<Long> signUp(String email, String password);

    UserDto getCurrentUser();

    boolean deleteUser();
}
