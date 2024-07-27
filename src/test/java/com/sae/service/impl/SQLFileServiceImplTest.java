package com.sae.service.impl;

import com.sae.models.request.SQLRequest;
import com.sae.repository.ExcelDataReadService;
import com.sae.repository.ExcelSQLGeneratorService;
import com.sae.repository.RequestsRepository;
import com.sae.repository.SQLFileService;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class SQLFileServiceImplTest {
    @Mock
    private RequestsRepository requestsRepository;

    @Mock
    private MultipartFile multipartFile;
    @Mock
    private ExcelSQLGeneratorService excelSQLGeneratorService;
    @Mock
    private ExcelDataReadService excelDataReadService;
    @Mock
    private SQLFileService sqlFileService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        excelDataReadService = new ExcelDataReaderServiceImpl();
        excelSQLGeneratorService = new ExcelSQLGeneratorServiceImpl();
        sqlFileService = new SQLFileServiceImpl();
    }

    @Test
    void saveSQLTest() throws IOException {
        // Load the test Excel file
        File file = new File("src/main/resources/tmp_data/test_select.xlsx");
        InputStream inputStream = new FileInputStream(file);
        multipartFile = new MockMultipartFile("file", inputStream);

        // Prepare mapping header
        Map<String, String> mappingHeader = new HashMap<>();
        mappingHeader.put("condition1", "order_number");
        mappingHeader.put("condition2", "status");
        mappingHeader.put("condition3", "store_information_id");

        //prepare columns array requestBody
        Map<String, String> columnsMapp = new HashMap<>();
        columnsMapp.put("column1","orders");
        // Prepare comparatives map
        Map<String, String> comparatives = new HashMap<>();
        comparatives.put("order_number", "=");
        comparatives.put("status", "=");
        comparatives.put("store_information_id", "LIKE");

        try{
            // Read data from the Excel file
            SQLRequest sqlQueryRequest = excelDataReadService.readExcelData(
                    multipartFile, "uq_au_db", "orders", columnsMapp, mappingHeader, comparatives,"=");
            System.out.println("Read Excel result: " + sqlQueryRequest);
            List<String> generatedSQLs = new ArrayList<>(excelSQLGeneratorService.generatorSQLFromRequest("SELECT", sqlQueryRequest));
            sqlFileService.saveSQLFile(generatedSQLs, sqlQueryRequest);

            //test check file is created and write correctly
            Path path = Path.of("src/main/resources/generate_result/test_select.sql");
            assertTrue(Files.exists(path));
            //test sample in DB and the data has been saved
            List<String> fileContent = Files.readAllLines(path);
            assertEquals(generatedSQLs.size(), fileContent.size(), "Mismatch in number of SQL queries written to file.");
            for (int i = 0; i < generatedSQLs.size(); i++) {
                assertEquals(generatedSQLs.get(i) + ";", fileContent.get(i), "Mismatch in SQL query content.");
            }
//            verify(requestsRepository, times(1)).save(any(Requests.class));
        }catch (Exception e){
            fail("Exception occurred during test execution: " + e.getMessage());
        }
    }
}