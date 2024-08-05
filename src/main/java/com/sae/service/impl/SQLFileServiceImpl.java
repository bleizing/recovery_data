package com.sae.service.impl;

import com.sae.entity.Requests;
import com.sae.entity.Users;
import com.sae.mapper.SQLQueryRequestMapper;
import com.sae.models.request.SQLRequest;
import com.sae.models.response.WebResponse;
import com.sae.repository.RequestsRepository;
import com.sae.service.SQLFileService;
import com.sae.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class SQLFileServiceImpl implements SQLFileService {
    @Autowired
    private RequestsRepository requestsRepository;
    @Autowired
    private UsersRepository usersRepository;
    private static final String DIRECTORY = "src/main/resources/generate_result";


    @Override
    public void saveSQLFile(List<String> queries, SQLRequest sqlQueryRequest) {
        File directory = new File(DIRECTORY);
        if (!directory.exists()) {
            directory.mkdir();
        }
        // Fetch the Users entity from database using userId
        Users users = usersRepository.findFirstByToken(sqlQueryRequest.getToken())
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Map SQLQueryRequest to Requests entity using the mapper
        Requests requests = SQLQueryRequestMapper.toEntity(sqlQueryRequest, users);
        // Verify that the essential fields in the requests entity are populated
        if (requests.getRegions() == null || requests.getTables() == null || requests.getColumns() == null) {
            throw new IllegalStateException("Failed to map SQLQueryRequest to Requests entity due to missing fields.");
        }
            Path filePath = Paths.get(DIRECTORY, sqlQueryRequest.getFileName() +".sql");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
                for (String query : queries) {
                    writer.write(query);
                    writer.newLine();
                }
                //if save to DB
//            if (sqlQueryRequest.getIsSaveToDB()){
//                requestsRepository.save(requests);
//            }
        }catch(Exception e){
            ResponseEntity.status(500).body(new WebResponse<>(null, "Failed save to path file and save to DB: " + e.getMessage()));
        }
    }

    @Override
    public Resource downloadFileSQL(String fileName) throws IOException {
//        will become soon
        return null;
    }
}
