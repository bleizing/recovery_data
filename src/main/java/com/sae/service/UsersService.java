package com.sae.service;

import com.sae.models.request.RegisterUsersRequest;

public interface UsersService {
    void register(RegisterUsersRequest requestUsers);
}
