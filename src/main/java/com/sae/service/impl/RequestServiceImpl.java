package com.sae.service.impl;

import com.sae.entity.Requests;
import com.sae.repository.RequestService;
import com.sae.repository.RequestsRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class RequestServiceImpl implements RequestService {
    @Autowired
    private RequestsRepository requestRepository;
    @Override
    public void saveRequest(Requests requests) {
        requestRepository.save(requests);
    }
}
