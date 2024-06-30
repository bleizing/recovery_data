package com.sae.service;

import com.sae.dto.SQLQueryRequest;
import com.sae.repository.ExcelDataReadService;
import com.sae.repository.ExcelSQLGeneratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ExcelSQLGeneratorServiceImplTest {

    @Mock
    private MultipartFile multipartFile;
    @Mock
    private ExcelSQLGeneratorService excelSQLGeneratorService;
    @Mock
    private ExcelDataReadService excelDataReadService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        excelDataReadService = new ExcelDataReaderServiceImpl();
        excelSQLGeneratorService = new ExcelSQLGeneratorServiceImpl();
    }

    @Test
    void testReadAndGenerate() throws IOException {
        // Load the test Excel file
        File file = new File("src/main/resources/tmp_data/test.xlsx");
        InputStream inputStream = new FileInputStream(file);
        multipartFile = new MockMultipartFile("file", inputStream);

        // Prepare mapping header
        Map<String, String> mappingHeader = new HashMap<>();
        mappingHeader.put("condition1", "orderId");
        mappingHeader.put("condition2", "status");
        //prepare columns array requestBody
        Map<String, String> columnsMapp = new HashMap<>();
        columnsMapp.put("column1","orders");

        // Read data from the Excel file
        try{

            // Read data from the Excel file
            SQLQueryRequest sqlQueryRequest = excelDataReadService.readExcelData(
                    multipartFile, "uq_jp_db", "orders", "=", columnsMapp, mappingHeader);
            System.out.println("Read Excel result: " + sqlQueryRequest);

            // Validate the result
            assertNotNull(sqlQueryRequest);
            assertEquals("uq_jp_db", sqlQueryRequest.getRegions());
            assertEquals("orders", sqlQueryRequest.getTables());

            // Generate the SQL query
            List<String> generatedSQLs = excelSQLGeneratorService.generatorSQLFromRequest("SELECT", sqlQueryRequest);

            // Expected SQL
            String expectedSQL1 = "SELECT * FROM orders WHERE orderId = '82228288' AND status = 'received'";
            String expectedSQL2 = "SELECT * FROM orders WHERE orderId = '8388813' AND status = 'shipped'";
            // Validate the generated SQLs
            System.out.println("Generated SQLs: " + generatedSQLs);
            assertEquals(4, generatedSQLs.size());
            assertEquals(expectedSQL1, generatedSQLs.get(0));
            assertEquals(expectedSQL2, generatedSQLs.get(1));
        }catch (Exception e){
            fail("Exception occurred during test execution: " + e.getMessage());
        }
    }
}