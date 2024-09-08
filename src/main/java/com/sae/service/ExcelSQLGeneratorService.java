package com.sae.service;

import com.sae.models.request.SQLRequest;

import java.util.List;


public interface ExcelSQLGeneratorService {
    List<String> generatorSQLFromRequest(String operations,SQLRequest sqlQueryRequest) throws Exception;
}
