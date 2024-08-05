package com.sae.service.impl;

import com.sae.entity.Users;
import com.sae.models.request.LoginUsersRequest;
import com.sae.models.response.TokenResponse;
import com.sae.repository.UsersRepository;
import com.sae.security.BCrypt;
import com.sae.service.AuthService;
import com.sae.service.ValidationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ValidationService validationService;

    @Override
    @Transactional
    public TokenResponse login(LoginUsersRequest loginUsersRequest) {
        validationService.validate(loginUsersRequest);

        Users users = usersRepository.findById(loginUsersRequest.getUsername())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username  or Password Wrong !"));

        //set token with BCrypt
        if (BCrypt.checkpw(loginUsersRequest.getPassword(), users.getPassword())) {
            users.setToken(UUID.randomUUID().toString());
            users.setTokenExpiredAt(expiredTime());
            usersRepository.save(users);
            return TokenResponse.builder()
                    .token(users.getToken())
                    .tokenExpiredAt(users.getTokenExpiredAt())
                    .build();
        }else{
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username  or Password Wrong !");
        }
    }

    //get time for set expired toke
    private long expiredTime(){
        LocalDateTime currentDateTime =  LocalDateTime.now();
        LocalDateTime expirationDateTime = currentDateTime.plusHours(1);
        // Convert the expiration time to epoch milliseconds
        Instant instant = expirationDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return instant.toEpochMilli();
    }

    @Override
    @Transactional
    public void logOut(Users users) {
        users.setToken(null);
        users.setTokenExpiredAt(null);

        usersRepository.save(users);
    }
}
