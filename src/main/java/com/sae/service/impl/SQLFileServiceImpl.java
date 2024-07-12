package com.sae.service.impl;

import com.sae.entity.Requests;
import com.sae.entity.Users;
import com.sae.mapper.SQLQueryRequestMapper;
import com.sae.models.request.SQLQueryRequest;
import com.sae.repository.RequestRepository;
import com.sae.repository.SQLFileService;
import com.sae.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class SQLFileServiceImpl implements SQLFileService {
    @Autowired
    private RequestRepository requestsRepository;
    @Autowired
    private UsersRepository usersRepository;
    private static final String DIRECTORY = "src/main/resources/generate_result";


    @Override
    public void saveSQLFile(List<String> queries,String operations, SQLQueryRequest sqlQueryRequest) throws IOException {
        File directory = new File(DIRECTORY);
        // Fetch the Users entity from database using userId
        Users users = usersRepository.findFirstByToken(sqlQueryRequest.getHeaders().get("TOKEN"))
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Map SQLQueryRequest to Requests entity using the mapper
        Requests requests = SQLQueryRequestMapper.toEntity(sqlQueryRequest, users);
        if (!directory.exists()) {
            directory.mkdir();
        }

            Path filePath = Paths.get(DIRECTORY, sqlQueryRequest.getFileName() +".sql");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
                for (String query : queries) {
                    writer.write(query);
                    writer.newLine();
                }
                //save to DB
            if (sqlQueryRequest.getIsSaveToDB()){
                requestsRepository.save(requests);
            }
    }

    @Override
    public Resource downloadFileSQL(String fileName) throws IOException {
        Path filePath = Paths.get(DIRECTORY, fileName + ".sql");
        if (!Files.exists(filePath)) {
            throw new IOException("File not found: " + fileName);
        }
        return new FileSystemResource(filePath.toFile());
    }
}
