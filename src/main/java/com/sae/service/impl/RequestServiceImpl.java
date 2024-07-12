package com.sae.service.impl;

import com.sae.entity.Requests;
import com.sae.repository.RequestService;
import com.sae.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class RequestServiceImpl implements RequestService {
    @Autowired
    private RequestRepository requestRepository;
    @Override
    public void saveRequest(Requests requests) {
        requestRepository.save(requests);
    }
}
