package com.sae.repository;

import com.sae.dto.SQLQueryRequest;

import java.util.List;

public interface ExcelSQLGeneratorService {
    List<String> generatorSQLFromRequest(String operations, SQLQueryRequest sqlQueryRequest) throws Exception;
}
