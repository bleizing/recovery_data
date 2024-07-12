package com.sae.controller;

import com.sae.models.request.RegisterUsersRequest;
import com.sae.models.response.WebResponse;
import com.sae.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersController {
    @Autowired
    private UsersService usersService;

    public WebResponse<String> register(@RequestBody RegisterUsersRequest request){
        usersService.register(request);
        return WebResponse.<String>builder().data("ok").build();
    }
}
