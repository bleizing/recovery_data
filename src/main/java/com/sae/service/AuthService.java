package com.sae.service;

import com.sae.entity.Users;
import com.sae.models.request.LoginUsersRequest;
import com.sae.models.response.TokenResponse;

public interface AuthService {
    TokenResponse login(LoginUsersRequest loginUsersRequest);
    void logOut(Users users);
    Users validateUsersToken(String token);
}
