package com.sae.service;

import com.sae.entity.Users;
import com.sae.models.request.RegisterUsersRequest;
import com.sae.repository.UsersRepository;
import com.sae.security.BCrypt;
import jakarta.transaction.Transactional;
import jakarta.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsersService {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private ValidationService validationService;

    @Transactional
    public void register(RegisterUsersRequest request) {
        validationService.validate(request);

        if (usersRepository.existsById(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already registered");
        }

        Users user = new Users();
        user.setUsername(request.getUsername());
        user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));

        usersRepository.save(user);
    }
}
