package com.sae.controller;


import com.sae.dto.SQLQueryRequest;
import com.sae.models.WebResponse;
import com.sae.repository.ExcelDataReadService;
import com.sae.repository.ExcelSQLGeneratorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(path = "/api")
@Validated
public class SQLGenerateController {
    @Autowired
    private ExcelDataReadService excelDataReadService;
    @Autowired
    private ExcelSQLGeneratorService excelSQLGeneratorService;

    @PostMapping("/{operations}")
    public ResponseEntity<WebResponse<List<String>>> generateSQL(
            @PathVariable String operations,
            @Valid @RequestBody SQLQueryRequest sqlQueryRequest,
            @RequestHeader("TOKEN") String token){
        try {
            List<String> generatedSQL = excelSQLGeneratorService.generatorSQLFromRequest(operations, sqlQueryRequest);
            return ResponseEntity.ok(new WebResponse<>(generatedSQL, null));
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(new WebResponse<>(null, "Invalid operation: " + operations));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new WebResponse<>(null, "Error generating SQL: " + e.getMessage()));
        }
    }

    @PostMapping("/readExcel")
    public ResponseEntity<WebResponse<SQLQueryRequest>> readExcelData(
            @RequestParam("file") MultipartFile file,
            @RequestParam("regions") String regions,
            @RequestParam("table") String tables,
            @RequestParam("comparative") String comparative,
            @RequestBody Map<String, String> columns,
            @RequestBody Map<String, String> mappingHeaders){
        try {
            SQLQueryRequest sqlQueryRequest = excelDataReadService.readExcelData(file, regions, tables,comparative, columns, mappingHeaders);
            return ResponseEntity.ok(new WebResponse<>(sqlQueryRequest, null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new WebResponse<>(null, "Error reading Excel file: " + e.getMessage()));
        }
    }
}
