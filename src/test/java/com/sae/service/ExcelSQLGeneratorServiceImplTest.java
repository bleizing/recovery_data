package com.sae.service;

import com.sae.models.request.SQLRequest;
import com.sae.service.impl.ExcelDataReaderServiceImpl;
import com.sae.service.impl.ExcelSQLGeneratorServiceImpl;
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
import java.util.ArrayList;
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
    void testReadAndGenerateSelectMoreConditions() throws IOException {
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

        // Read data from the Excel file
        try{
            // Read data from the Excel file
            SQLRequest sqlQueryRequest = excelDataReadService.readExcelData(
                    multipartFile, "uq_au_db", "orders",null, mappingHeader, comparatives,"=");
            System.out.println("Read Excel result: " + sqlQueryRequest);
            // Validate the result
            assertNotNull(sqlQueryRequest);
            assertEquals("uq_au_db", sqlQueryRequest.getRegions());
            assertEquals("orders", sqlQueryRequest.getTables());

            List<String> generatedSQLs = new ArrayList<>(excelSQLGeneratorService.generatorSQLFromRequest("SELECT", sqlQueryRequest));

            // Expected SQL
            String expectedSQL1 = "SELECT uq_au_db.orders WHERE order_number = '1420011112406131435-0806620' AND status = 'received' AND store_information_id LIKE 12";
            String expectedSQL2 = "SELECT uq_au_db.orders WHERE order_number = '1420011112406131435-0806622' AND status = 'received' AND store_information_id LIKE 12";
            String expectedSQL3 = "SELECT uq_au_db.orders WHERE order_number = '1420011112406131435-0806624' AND status = 'received' AND store_information_id LIKE 12";

            // Validate the generated SQLs
            System.out.println("Generated SQLs: " + generatedSQLs);
            assertEquals(3, generatedSQLs.size());
            assertEquals(expectedSQL1, generatedSQLs.get(0));
            assertEquals(expectedSQL2, generatedSQLs.get(1));
            assertEquals(expectedSQL3, generatedSQLs.get(2));


        }catch (Exception e){
            fail("Exception occurred during test execution: " + e.getMessage());
        }
    }

    @Test
    void testReadAndGenerateDeletedMoreConditions() throws IOException {
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

        // Read data from the Excel file
        try{

            // Read data from the Excel file
            SQLRequest sqlQueryRequest = excelDataReadService.readExcelData(
                    multipartFile, "uq_au_db", "orders",null, mappingHeader, comparatives,"=");
            System.out.println("Read Excel result: " + sqlQueryRequest);
            // Validate the result
            assertNotNull(sqlQueryRequest);
            assertEquals("uq_au_db", sqlQueryRequest.getRegions());
            assertEquals("orders", sqlQueryRequest.getTables());

            List<String> generatedSQLs = new ArrayList<>(excelSQLGeneratorService.generatorSQLFromRequest("DELETE", sqlQueryRequest));

            // Expected SQL
            String expectedSQL1 = "DELETE uq_au_db.orders WHERE order_number = '1420011112406131435-0806620' AND status = 'received' AND store_information_id LIKE 12";
            String expectedSQL2 = "DELETE uq_au_db.orders WHERE order_number = '1420011112406131435-0806622' AND status = 'received' AND store_information_id LIKE 12";
            String expectedSQL3 = "DELETE uq_au_db.orders WHERE order_number = '1420011112406131435-0806624' AND status = 'received' AND store_information_id LIKE 12";

            // Validate the generated SQLs
            System.out.println("Generated SQLs: " + generatedSQLs);
            assertEquals(3, generatedSQLs.size());
            assertEquals(expectedSQL1, generatedSQLs.get(0));
            assertEquals(expectedSQL2, generatedSQLs.get(1));
            assertEquals(expectedSQL3, generatedSQLs.get(2));


        }catch (Exception e){
            fail("Exception occurred during test execution: " + e.getMessage());
        }
    }

    @Test
    void testReadAndGenerateUpdateSQL()throws IOException {
        // Load the test Excel file
        File file = new File("src/main/resources/tmp_data/test_update.xlsx");
        InputStream inputStream = new FileInputStream(file);
        MockMultipartFile multipartFile = new MockMultipartFile("file", inputStream);

        // Prepare mapping header
        Map<String, String> mappingHeader = new HashMap<>();
        mappingHeader.put("setValue1", "status");
        mappingHeader.put("setValue2", "last_update_date");
        mappingHeader.put("condition1", "order_id");
        mappingHeader.put("condition2", "type_order");



        // Prepare comparatives map
        Map<String, String> comparatives = new HashMap<>();
        comparatives.put("last_update_date", "=");
        comparatives.put("status", "=");
        comparatives.put("order_id", "=");
        comparatives.put("type_order", "=");

        try {
            // Read data from the Excel file
            SQLRequest sqlQueryRequest = excelDataReadService.readExcelData(
                    multipartFile, "public", "orders",null, mappingHeader, comparatives, "=");
            System.out.println("Read Excel result: " + sqlQueryRequest);
            assertNotNull(sqlQueryRequest);
            assertEquals("public", sqlQueryRequest.getRegions());
            assertEquals("orders", sqlQueryRequest.getTables());

            List<String> generatedSQLs = excelSQLGeneratorService.generatorSQLFromRequest("UPDATE", sqlQueryRequest);
            System.out.println("Generated SQLs: " + generatedSQLs);
            assertEquals(2, generatedSQLs.size());

            // Expected SQL
            String expectedSQL1 = "UPDATE public.orders SET last_update_date = now(), status = 'DELETED' WHERE order_id = 1209380 AND type_order = 'SALE';";
            String expectedSQL2 = "UPDATE public.orders SET last_update_date = now(), status = 'DELETED' WHERE order_id = 1212121 AND type_order = 'SALE';";

            assertEquals(expectedSQL1, generatedSQLs.get(0));
            assertEquals(expectedSQL2, generatedSQLs.get(1));
        } catch (Exception e) {
            fail("Exception occurred during execution update generate test: " + e.getMessage());
        }
    }
    @Test
    void testRealDataUpdateSQL() throws Exception {
        // Load the test Excel file
        File file = new File("src/main/resources/tmp_data/Label_change.xlsx");
        InputStream inputStream = new FileInputStream(file);
        multipartFile = new MockMultipartFile("file", inputStream);

        // Prepare mapping header
        Map<String, String> mappingHeader = new HashMap<>();
        mappingHeader.put("setValue1", "label_title");
        mappingHeader.put("setValue2", "sorting_code");
        mappingHeader.put("setValue3", "last_update_date");
        mappingHeader.put("condition1", "identification_id");
        mappingHeader.put("condition2", "zip_code");

        // Prepare comparatives map
        Map<String, String> comparatives = new HashMap<>();
        comparatives.put("identification_id", "=");
        comparatives.put("zip_code", "=");

            // Read data from the Excel file
            SQLRequest sqlQueryRequest = excelDataReadService.readExcelData(
                    multipartFile, "uq_jp_db", "zip_code_sorting_master",null, mappingHeader, comparatives,"=");
            System.out.println("Read Excel result: " + sqlQueryRequest);
            assertNotNull(sqlQueryRequest);
            assertEquals("uq_jp_db", sqlQueryRequest.getRegions());
            assertEquals("zip_code_sorting_master", sqlQueryRequest.getTables());

        List<String> generatedSQLs = new ArrayList<>(excelSQLGeneratorService.generatorSQLFromRequest("UPDATE", sqlQueryRequest));
            assertEquals(293, sqlQueryRequest.getTotalRows());

    }
}