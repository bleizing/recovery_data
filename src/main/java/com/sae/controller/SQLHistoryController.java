package com.sae.controller;

import com.sae.repository.SQLFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/sql")
public class SQLHistoryController {
    @Autowired
    private SQLFileService sqlFileService;
    @PostMapping("/generate")
    public ResponseEntity<String> generateSQLFile(@RequestBody List<String> queries, @RequestParam String fileName){
        try {
            String filePath = sqlFileService.saveSQLFile(queries,operations, fileName);
            return ResponseEntity.ok("File saved at: " + filePath);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error saving file: " + e.getMessage());
        }
    }
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadSQLFile(@RequestParam String fileName) {
        try {
            Resource file = sqlFileService.downloadFileSQL(fileName);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                    .body(file);
        } catch (IOException e) {
            return ResponseEntity.status(404).body(null);
        }
    }
}
