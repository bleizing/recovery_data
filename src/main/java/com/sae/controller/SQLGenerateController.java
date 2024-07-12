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
            @Valid @RequestBody SQLQueryRequest sqlQueryRequest){
        try {
            List<String> generatedSQL = excelSQLGeneratorService.generatorSQLFromRequest(operations, sqlQueryRequest);
            sqlFileService.saveSQLFile(generatedSQL,operations, sqlQueryRequest);
            return WebResponse.<String>builder().data("OK").build();
        }catch (IllegalArgumentException e){
            return
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new WebResponse<>(null, "Error generating SQL: " + e.getMessage()));
        }
    }

    @PostMapping("/readExcel")
    public ResponseEntity<WebResponse<SQLQueryRequest>> readExcelData(
            @RequestParam("file") MultipartFile file,
            @RequestParam("regions") String regions,
            @RequestParam("table") String tables,
            @RequestBody Map<String, String> columns,
            @RequestBody Map<String, String> mappingHeaders,
            @RequestBody Map<String, String> comparatives,
            @RequestParam(value = "defaultComparative", defaultValue = "=") String defaultComparative){
        try {
            SQLQueryRequest sqlQueryRequest = excelDataReadService.readExcelData(file, regions, tables,columns, mappingHeaders,comparatives,defaultComparative);
            return ResponseEntity.ok(new WebResponse<>(sqlQueryRequest, null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new WebResponse<>(null, "Error reading Excel file: " + e.getMessage()));
        }
    }
}
