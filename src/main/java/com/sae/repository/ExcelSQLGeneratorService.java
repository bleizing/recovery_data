package com.sae.repository;

import com.sae.models.request.SQLQueryRequest;

import java.util.List;


public interface ExcelSQLGeneratorService {
    List<String> generatorSQLFromRequest(String operations, SQLQueryRequest sqlQueryRequest) throws Exception;
}
