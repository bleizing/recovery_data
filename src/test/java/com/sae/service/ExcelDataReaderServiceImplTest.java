package com.sae.service;

import com.sae.dto.SQLQueryRequest;
import com.sae.repository.ExcelDataReadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

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
        // Load the test Excel file
        File file = new File("src/main/resources/tmp_data/test.xlsx");
        InputStream inputStream = new FileInputStream(file);
        multipartFile = new MockMultipartFile("file", inputStream);

        // Prepare mapping header
        Map<String, String> mappingHeader = new HashMap<>();

        Map<String, String> mappingColumns = new HashMap<>();
        mappingColumns.put("orderId", "A"); // Column A for orderId
        mappingColumns.put("status", "B");  // Column B for status
        // Read data from the Excel file
        SQLQueryRequest result = excelDataReadService.readExcelData(
                multipartFile, "uq_jp_db", "orders", "=", null ,null);

        // Validate the result
        assertNotNull(result);
        assertEquals("uq_jp_db", result.getRegions());
        assertEquals("orders", result.getTables());

        // Validate the conditions
        SQLQueryRequest.SetConditions setCondition = result.getConditions().get(0);
        assertNotNull(setCondition);
        assertEquals("82228288", setCondition.getValues());
        assertEquals("=", setCondition.getComparative());

        // Validate the set values
        SQLQueryRequest.SetValue setValue = result.getSetValues().get(0);
        assertNotNull(setValue);
        assertEquals("received", setValue.getValue());
    }
}