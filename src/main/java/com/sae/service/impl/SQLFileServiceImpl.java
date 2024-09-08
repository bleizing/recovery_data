package com.sae.service.impl;

import com.sae.entity.Requests;
import com.sae.entity.Users;
import com.sae.mapper.SQLQueryRequestMapper;
import com.sae.models.request.SQLRequest;
import com.sae.models.response.GenerateResponse;
import com.sae.models.response.WebResponse;
import com.sae.repository.RequestsRepository;
import com.sae.service.AuthService;
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
    @Autowired
    private AuthService authService;
    @Autowired
    private GenerateResponse generateResponse;
    private static final String DIRECTORY = "src/main/resources/generate_result";


    @Override
    public WebResponse<String> saveSQLFile(List<String> queries, SQLRequest sqlQueryRequest) {
        File directory = new File(DIRECTORY);
        if (!directory.exists()) {
            directory.mkdir();
        }

//        // Validate user
        Users users = new Users();
        users.setName("Jon Bigger");
        users.setPassword("salah");
        users.setUsername("sample");
        users.setTokenExpiredAt(3000L);
        users.setToken("usoiasoa");
        System.out.println(" getRegions: "+sqlQueryRequest.getRegions()+" getTables: "+sqlQueryRequest.getTables()+"getColumns: "+sqlQueryRequest.getColumns()+"getSetValues: "+sqlQueryRequest.getSetValues()+"getConditions:"+sqlQueryRequest.getConditions());
//        // Map SQLQueryRequest to Requests entity using the mapper
        Requests requests = SQLQueryRequestMapper.toEntity(sqlQueryRequest, users);
        // Verify that the essential fields in the requests entity are populated
        Path filePath = Paths.get(DIRECTORY, sqlQueryRequest.getFileName() +".sql");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
                for (String query : queries) {
                    writer.write(query);
                    writer.newLine();
                }
        requestsRepository.save(requests);
        }catch(Exception e){
            ResponseEntity.status(500).body(new WebResponse<>(null, "Failed save to path file and save to DB: " + e.getMessage()));
        }
        return WebResponse.<String>builder().data(String.valueOf(filePath)).build();
    }

    @Override
    public Resource downloadFileSQL(String fileName) throws IOException {
//        will become soon
        return null;
    }
}
