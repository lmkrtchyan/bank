package com.lm.bank.service;


import com.lm.bank.busobj.response.GeneralResponse;

public interface AuthService {
    GeneralResponse login(String username, String password);

    GeneralResponse logout();

    Boolean isAuthenticated();
}
