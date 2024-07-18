package com.sae.controller;


import com.sae.models.request.SQLQueryRequest;
import com.sae.entity.SetConditions;
import com.sae.entity.Requests;
import com.sae.entity.SetValue;
import com.sae.entity.Users;
import com.sae.models.response.WebResponse;
import com.sae.repository.ExcelDataReadService;
import com.sae.repository.ExcelSQLGeneratorService;
import com.sae.repository.SQLFileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping(path = "/api")
@Validated
public class SQLGenerateController {
    @Autowired
    private ExcelDataReadService excelDataReadService;
    @Autowired
    private ExcelSQLGeneratorService excelSQLGeneratorService;
    @Autowired
    private SQLFileService sqlFileService;

    @PostMapping(
                path = "/generateSQL/{operations}",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public WebResponse<String> generateSQL(
            @PathVariable String operations,
            @RequestHeader("user_id") String userId,
            @RequestHeader("token") String token,
            @RequestParam("file") MultipartFile file,
            @RequestParam("regions") String regions,
            @RequestParam("tables") String tables,
            @RequestParam Map<String, String> columns,
            @RequestParam Map<String, String> mappingHeaders,
            @RequestParam Map<String, String> comparatives,
            @RequestParam(value = "defaultComparative", defaultValue = "=") String defaultComparative,
            @RequestParam("isSaveToDB") Boolean isToDB,
            @RequestParam("fileName") String fileName,
            @Valid @RequestBody SQLQueryRequest sqlQueryRequest){
        try {
           //read excel
            SQLQueryRequest requestFromExcel =  excelDataReadService.readExcelData(
                    file,
                    regions,
                    tables,
                    columns,
                    mappingHeaders,
                    comparatives,
                    defaultComparative
            );
            //read excel
            //merge requestFromExcel data into sqlQueryRequest
            sqlQueryRequest.setRegions(requestFromExcel.getRegions());
            sqlQueryRequest.setTables(requestFromExcel.getTables());
            sqlQueryRequest.setColumns(requestFromExcel.getColumns());
            sqlQueryRequest.setSetValues(requestFromExcel.getSetValues());
            sqlQueryRequest.setConditions(requestFromExcel.getConditions());
            sqlQueryRequest.setIsSaveToDB(isToDB);
            sqlQueryRequest.setFileName(fileName);

            //set userId
            sqlQueryRequest.setUserId(userId);
            sqlQueryRequest.setToken(token);

            //generate , save and upload to DB
            List<String> generatedSQL = excelSQLGeneratorService.generatorSQLFromRequest(operations, sqlQueryRequest);
            // Check if generatedSQL is null and throw an IOException if it is
            if (generatedSQL == null || generatedSQL.isEmpty()) {
                throw new IOException("Generated SQL is null or empty.");
            }
            sqlFileService.saveSQLFile(generatedSQL, operations, sqlQueryRequest);
            //save if success generate
            return WebResponse.<String>builder().data("true").build();
        } catch (Exception e) {
            return WebResponse.<String>builder().data("false {}"+ e).build();
        }
    }

}
