package com.sae.repository;

import com.sae.models.request.SQLRequest;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

public interface SQLFileService {
    void saveSQLFile(List<String> queries, SQLRequest sqlQueryRequest) throws IOException;
    Resource downloadFileSQL(String fileName) throws IOException;
}
