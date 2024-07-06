package com.sae.repository;

import com.sae.dto.SQLQueryRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface ExcelDataReadService {
    SQLQueryRequest readExcelData(MultipartFile file,
                                  String regions,
                                  String tables,
                                  Map<String, String> columns,
                                  Map<String, String> mappingHeaders,
                                  Map<String, String> comparatives,
                                  String defaultComparative
                                  ) throws Exception;
}
