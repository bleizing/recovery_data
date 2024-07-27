package com.sae.service.impl;

import com.sae.models.request.SQLRequest;
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
    public SQLRequest readExcelData(MultipartFile file,
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
//                System.out.println("header number : " + headerRow.getRowNum());
                columnMapping.put(getCellValueAsString(cell), cell.getColumnIndex());
            }
            int conditionsPerQuery = columnMapping.size();

            // Initialize lists to store conditions and values
            List<SQLRequest.SetConditions> setConditionsList = new ArrayList<>();
            List<SQLRequest.SetValue> setValuesList = new ArrayList<>();
            int totalRows = 0;

            for (Row row : sheet) {
                int a = 0;
                // Skip header
                if (row.getRowNum() == 0) {
                    continue;
                }

                // Count the row
                totalRows++;

                // Iterate through the mapping header for condition
                for (Map.Entry<String, String> entry : mappingHeaders.entrySet()) {
                    String key = entry.getKey();
                    String column = entry.getValue();
                    // Ensure the column is in the mapping
                    if (!columnMapping.containsKey(column)) {
                        throw new IllegalArgumentException("Column '" + column + "' not found in the Excel file.");
                    }
                    String data = getCellValueAsString(row.getCell(columnMapping.get(column)));
//                    System.out.println("data columnMapping : " + data + " | iteration read : " + a++);
                    if (key.startsWith("condition")) {
                        SQLRequest.SetConditions setCondition = new SQLRequest.SetConditions();
                        setCondition.setColumns(column);
                        setCondition.setValues(data);
                        // Set the comparative value from the comparatives map or use the default
                        setCondition.setComparative(comparatives.getOrDefault(column, defaultComparative));
                        setConditionsList.add(setCondition);
                    } else if (key.startsWith("setValue")) {
                        SQLRequest.SetValue setValue = new SQLRequest.SetValue();
                        setValue.setColumns(column);
                        setValue.setValue(data);
                        setValuesList.add(setValue);
                    }
                }
                conditionsPerQuery = Math.max(conditionsPerQuery, mappingHeaders.size());
            }

            SQLRequest request = SQLRequest.builder()
                    .regions(regions) // Use the parameter
                    .tables(tables) // Use the parameter
                    .conditions(setConditionsList) // Use the list
                    .setValues(setValuesList) // Use the list
                    .conditionsPerQuery(conditionsPerQuery)
                    .build();
            request.setTotalRows(totalRows);
            return request;
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
                return switch (cell.getCachedFormulaResultType()) {
                    case STRING -> cell.getRichStringCellValue().getString();
                    case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
                    case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
                    default -> "";
                };
            case BLANK:
                return "";
            default:
                return "";
        }
    }
}
