package com.sae.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HistoryQueryRepositoryTest {

    @Autowired
    private HistoryQueryService historyQueryRepository;
    @Autowired
    private UsersRepository usersRepository;

    @Test
    void createHistoryQuery() {
            
    }
}