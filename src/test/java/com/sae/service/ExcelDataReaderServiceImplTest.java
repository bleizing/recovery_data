package com.sae.service;

import com.sae.repository.ExcelDataReadService;
import com.sae.service.impl.ExcelDataReaderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

class ExcelDataReaderServiceImplTest {

    @Mock
    private MultipartFile multipartFile;
    @Mock
    private ExcelDataReadService excelDataReadService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        excelDataReadService = new ExcelDataReaderServiceImpl();
    }

    @Test
    void testRead() throws Exception {

    }
}