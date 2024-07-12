package com.sae.repository;

import com.sae.entity.Users;
import com.sae.models.request.SQLQueryRequest;
import com.sae.models.request.SaveSQLRequest;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

public interface SQLFileService {
    void saveSQLFile(List<String> queries,String operations, SQLQueryRequest sqlQueryRequest) throws IOException;
    Resource downloadFileSQL(String fileName) throws IOException;
}
