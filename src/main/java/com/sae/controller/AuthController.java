package com.sae.controller;

import com.sae.models.request.LoginUsersRequest;
import com.sae.models.response.TokenResponse;
import com.sae.models.response.WebResponse;
import com.sae.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(
            path = "/api/auth/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TokenResponse> login(@RequestBody LoginUsersRequest loginUsersRequest){
        TokenResponse tokenResponse = authService.login(loginUsersRequest);
        return WebResponse.<TokenResponse>builder().data(tokenResponse).build();
    }

}
