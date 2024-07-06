package com.sae.service;

import com.sae.dto.SQLQueryRequest;
import com.sae.repository.ExcelDataReadService;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExcelDataReaderServiceImpl implements ExcelDataReadService {
    @Override
    public SQLQueryRequest readExcelData(MultipartFile file,
                                         String regions,
                                         String tables,
                                         Map<String, String> columns ,
                                         Map<String, String> mappingHeaders,
                                         Map<String, String> comparatives,
                                         String defaultComparative) throws Exception {
        try (InputStream is = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);

            // Read the header row to determine the mapping
            Map<String, Integer> columnMapping = new HashMap<>();
            Row headerRow = sheet.getRow(0);
            for (Cell cell : headerRow) {
                System.out.println(headerRow.getRowNum());
                columnMapping.put(getCellValueAsString(cell), cell.getColumnIndex());
            }
            int conditionsPerQuery = columnMapping.size();
            // Initialize lists to store conditions and values
            List<SQLQueryRequest.SetConditions> setConditionsList = new ArrayList<>();
            List<SQLQueryRequest.SetValue> setValuesList = new ArrayList<>();

            for (Row row : sheet) {
                int a = 0;
                // Skip header
                if (row.getRowNum() == 0) {
                    continue;
                }

                // Iterate through the mapping header for condition
                for (Map.Entry<String, String> entry : mappingHeaders.entrySet()) {
                    String key = entry.getKey();
                    String column = entry.getValue();
                    String data = getCellValueAsString(row.getCell(columnMapping.get(column)));

                    if (key.startsWith("condition")) {
                        SQLQueryRequest.SetConditions setCondition = new SQLQueryRequest.SetConditions();
                        setCondition.setColumns(column);
                        setCondition.setValues(data);
                        // Set the comparative value from the comparatives map or use the default
                        setCondition.setComparative(comparatives.getOrDefault(column, defaultComparative));
                        setConditionsList.add(setCondition);
                    } else if (key.startsWith("setValue")) {
                        SQLQueryRequest.SetValue setValue = new SQLQueryRequest.SetValue();
                        setValue.setColumns(column);
                        setValue.setValue(data);
                        setValuesList.add(setValue);
                    }
                }
                System.out.println("iteration read : "+ a);

            }

            return SQLQueryRequest.builder()
                    .regions(regions) // Use the parameter
                    .tables(tables) // Use the parameter
                    .conditions(setConditionsList) // Use the list
                    .setValues(setValuesList) // Use the list
                    .conditionsPerQuery(conditionsPerQuery)
                    .build();
        }
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return ""; // Return an empty string if the cell is null
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((long) cell.getNumericCellValue()); // Handle numeric as long if integer
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                switch (cell.getCachedFormulaResultType()) {
                    case STRING:
                        return cell.getRichStringCellValue().getString();
                    case NUMERIC:
                        return String.valueOf((long) cell.getNumericCellValue());
                    case BOOLEAN:
                        return String.valueOf(cell.getBooleanCellValue());
                    default:
                        return "";
                }
            case BLANK:
                return "";
            default:
                return "";
        }
    }
}
