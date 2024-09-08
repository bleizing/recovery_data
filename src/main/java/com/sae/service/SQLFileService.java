package com.sae.service;

import com.sae.models.request.SQLRequest;
import com.sae.models.response.WebResponse;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

public interface SQLFileService {
    WebResponse<String> saveSQLFile(List<String> queries, SQLRequest sqlQueryRequest) throws IOException;
    Resource downloadFileSQL(String fileName) throws IOException;
}
