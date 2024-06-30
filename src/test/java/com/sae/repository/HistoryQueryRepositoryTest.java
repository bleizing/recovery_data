package com.sae.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HistoryQueryRepositoryTest {

    @Autowired
    private HistoryQueryRepository historyQueryRepository;
    @Autowired
    private UsersRepository usersRepository;

    @Test
    void createHistoryQuery() {
            
    }
}