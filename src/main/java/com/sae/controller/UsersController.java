package com.sae.controller;

import com.sae.models.request.RegisterUsersRequest;
import com.sae.models.response.WebResponse;
import com.sae.service.impl.UsersServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersController {
    @Autowired
    private UsersServiceImpl usersService;

    @PostMapping(
            path = "/api/users",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> register(@RequestBody RegisterUsersRequest request){
        usersService.register(request);
        return WebResponse.<String>builder().data("OK").build();
    }
}
