package com.sae.service.impl;

import com.sae.models.request.SQLRequest;
import com.sae.service.ExcelSQLGeneratorService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ExcelSQLGeneratorServiceImpl implements ExcelSQLGeneratorService {
    @Override
    public List<String> generatorSQLFromRequest(String operations, SQLRequest sqlQueryRequest) {
        return switch (operations.toUpperCase()) {
            case "SELECT" -> generateSelectSQL(sqlQueryRequest);
            case "DELETE" -> generateDeleteSQL(sqlQueryRequest);
            case "UPDATE" -> generateUpdateSQL(sqlQueryRequest);
            case "CUSTOM" -> generateCustomSQL(sqlQueryRequest);
            default -> throw new IllegalArgumentException("Unsupported operation: " + operations);
        };
    }

    private List<String> generateUpdateSQL(SQLRequest sqlQueryRequest) {
        List<String> queries = new ArrayList<>();
        List<SQLRequest.SetConditions> conditions = sqlQueryRequest.getConditions();
        List<SQLRequest.SetValue> setValues = sqlQueryRequest.getSetValues();
        int totalRows = sqlQueryRequest.getTotalRows(); // Menggunakan totalRows dari request

        System.out.println("Total SetValue: " + setValues.size());
        System.out.println("Total conditions: " + conditions.size());
        System.out.println("Total rows: " + totalRows);

        for (int i = 0; i < totalRows; i++) {
            StringBuilder sql = new StringBuilder("UPDATE ");
            sql.append(sqlQueryRequest.getRegions())
                    .append(".")
                    .append(sqlQueryRequest.getTables())
                    .append(" SET ");

            // Tambahkan setValues digabung dengan koma
            boolean firstValue = true;
            for (int j = 0; j < setValues.size() / totalRows; j++) {
                if (!firstValue) {
                    sql.append(", ");
                }
                SQLRequest.SetValue setValue = setValues.get(j + i * (setValues.size() / totalRows));
                sql.append(setValue.getColumns()).append(" = ");
                String value = setValue.getValue();

                // Periksa apakah value numerik atau mengandung fungsi khusus
                if (isNumeric(value) || value.matches(".*\\(.*\\)")) {
                    sql.append(value);
                } else {
                    sql.append("'").append(value).append("'");
                }

                firstValue = false;
            }

            sql.append(" WHERE ");

            // Tambahkan kondisi digabung dengan AND
            boolean firstCondition = true;
            for (int j = 0; j < conditions.size() / totalRows; j++) {
                if (!firstCondition) {
                    sql.append(" AND ");
                }
                SQLRequest.SetConditions setCondition = conditions.get(j + i * (conditions.size() / totalRows));
                sql.append(setCondition.getColumns())
                        .append(" ")
                        .append(setCondition.getComparative())
                        .append(" ");

                String value = setCondition.getValues();

                // Periksa apakah value numerik atau mengandung fungsi khusus
                if (isNumeric(value) || value.matches(".*\\(.*\\)")) {
                    sql.append(value);
                } else {
                    sql.append("'").append(value).append("'");
                }

                firstCondition = false;
            }

            sql.append(";");
            queries.add(sql.toString());
        }

        System.out.println("Generated SQL queries: " + queries);
        return queries;
    }

    private List<String> generateDeleteSQL(SQLRequest sqlQueryRequest) {
        List<String> queries = new ArrayList<>();
        List<SQLRequest.SetConditions> conditions = sqlQueryRequest.getConditions();
        System.out.println("size conditionsPerQuery= "+sqlQueryRequest.getConditionsPerQuery());
        int conditionsPerQuery = sqlQueryRequest.getConditionsPerQuery(); // Assuming 4 conditions per query as per your data structure

        for (int i = 0; i < conditions.size(); i += conditionsPerQuery) {
            StringBuilder sql = new StringBuilder("DELETE ");
            sql.append(sqlQueryRequest.getRegions())
                    .append(".")
                    .append(sqlQueryRequest.getTables())
                    .append(" WHERE ");

            // Add conditions combined with AND
            boolean firstCondition = true;
            for (int j = i; j < i + conditionsPerQuery && j < conditions.size(); j++) {
                SQLRequest.SetConditions condition = conditions.get(j);
                if (!firstCondition) {
                    sql.append(" AND ");
                }
                sql.append(condition.getColumns())
                        .append(" ")
                        .append(condition.getComparative())
                        .append(" ");

                // Check if the value is numeric (integer)
                String value = condition.getValues();
                if (isNumeric(value) || value.matches(".*\\(.*\\)")){
                    sql.append(condition.getValues()); // Append without quotes if numeric
                }else{
                    sql.append("'").append(condition.getValues()).append("'");
                }
                firstCondition = false;
            }

            queries.add(sql.toString());
        }

        return queries;

    }


    private List<String> generateSelectSQL(SQLRequest sqlQueryRequest) {
        List<String> queries = new ArrayList<>();
        List<SQLRequest.SetConditions> conditions = sqlQueryRequest.getConditions();
        System.out.println("size conditionsPerQuery= "+sqlQueryRequest.getConditionsPerQuery());
        int conditionsPerQuery = sqlQueryRequest.getConditionsPerQuery(); // Assuming 4 conditions per query as per your data structure

        for (int i = 0; i < conditions.size(); i += conditionsPerQuery) {
            StringBuilder sql = new StringBuilder("SELECT ");
            sql.append(sqlQueryRequest.getRegions())
                    .append(".")
                    .append(sqlQueryRequest.getTables())
                    .append(" WHERE ");

            // Add conditions combined with AND
            boolean firstCondition = true;
            for (int j = i; j < i + conditionsPerQuery && j < conditions.size(); j++) {
                SQLRequest.SetConditions condition = conditions.get(j);
                if (!firstCondition) {
                    sql.append(" AND ");
                }
                sql.append(condition.getColumns())
                        .append(" ")
                        .append(condition.getComparative())
                        .append(" ");

                    // Check if the value is numeric (integer)
                    String value = condition.getValues();
                    if (isNumeric(value) || value.matches(".*\\(.*\\)")){
                        sql.append(condition.getValues()); // Append without quotes if numeric
                    }else{
                        sql.append("'").append(condition.getValues()).append("'");
                    }
                firstCondition = false;
            }

            queries.add(sql.toString());
        }

        return queries;
    }
    private List<String> generateCustomSQL(SQLRequest sqlQueryRequest) {
        // Implementation for generating CUSTOM SQL
        List<String> queries = new ArrayList<>();
        queries.add("CUSTOM SQL QUERY");
        return queries;
    }

    private boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }


}
