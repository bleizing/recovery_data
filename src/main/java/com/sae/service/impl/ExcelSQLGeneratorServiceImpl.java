package com.sae.service.impl;

import com.sae.models.request.SQLQueryRequest;
import com.sae.repository.ExcelSQLGeneratorService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ExcelSQLGeneratorServiceImpl implements ExcelSQLGeneratorService {
    @Override
    public List<String> generatorSQLFromRequest(String operations, SQLQueryRequest sqlQueryRequest) throws Exception {
        switch (operations.toUpperCase()) {
            case "SELECT":
                return generateSelectSQL(sqlQueryRequest);
            case "DELETE":
                return generateDeleteSQL(sqlQueryRequest);
            case "UPDATE":
                return generateUpdateSQL(sqlQueryRequest);
            case "CUSTOM":
                return generateCustomSQL(sqlQueryRequest);
            default:
                throw new IllegalArgumentException("Unsupported operation: " + operations);
        }}

    private List<String> generateUpdateSQL(SQLQueryRequest sqlQueryRequest) {
        List<String> queries = new ArrayList<>();
        List<SQLQueryRequest.SetConditions> conditions = sqlQueryRequest.getConditions();
        List<SQLQueryRequest.SetValue> setValues = sqlQueryRequest.getSetValues();

        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(sqlQueryRequest.getRegions())
                .append(".")
                .append(sqlQueryRequest.getTables())
                .append(" SET ");

        // Add set values combined with commas
        boolean firstValue = true;
        for (SQLQueryRequest.SetValue setValue : setValues) {
            if (!firstValue) {
                sql.append(", ");
            }
            sql.append(setValue.getColumns()).append(" = ");
            String value = setValue.getValue();

            // Check if value is numeric or contains specific function
            if (isNumeric(value) || value.matches(".*\\(.*\\)")) {
                sql.append(value);
            } else {
                sql.append("'").append(value).append("'");
            }

            firstValue = false;
        }

        sql.append(" WHERE ");

        // Add conditions combined with AND
        boolean firstCondition = true;
        for (SQLQueryRequest.SetConditions condition : conditions) {
            if (!firstCondition) {
                sql.append(" AND ");
            }
            sql.append(condition.getColumns())
                    .append(" ")
                    .append(condition.getComparative())
                    .append(" ");

            String conditionValue = condition.getValues();
            if (isNumeric(conditionValue) || conditionValue.matches(".*\\(.*\\)")) {
                sql.append(conditionValue);
            } else {
                sql.append("'").append(conditionValue).append("'");
            }

            firstCondition = false;
        }
        sql.append(";");
        queries.add(sql.toString());

        return queries;
    }

    private List<String> generateDeleteSQL(SQLQueryRequest sqlQueryRequest) {
        List<String> queries = new ArrayList<>();
        List<SQLQueryRequest.SetConditions> conditions = sqlQueryRequest.getConditions();
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
                SQLQueryRequest.SetConditions condition = conditions.get(j);
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


    private List<String> generateSelectSQL(SQLQueryRequest sqlQueryRequest) {
        List<String> queries = new ArrayList<>();
        List<SQLQueryRequest.SetConditions> conditions = sqlQueryRequest.getConditions();
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
                SQLQueryRequest.SetConditions condition = conditions.get(j);
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
    private List<String> generateCustomSQL(SQLQueryRequest sqlQueryRequest) {
        // Implementation for generating CUSTOM SQL
        List<String> queries = new ArrayList<>();
        queries.add("CUSTOM SQL QUERY");
        return queries;
    }
    private String formatConditions(Map<String, SQLQueryRequest.SetConditions> conditions) {
        StringBuilder conditionStr = new StringBuilder();
        for (Map.Entry<String, SQLQueryRequest.SetConditions> entry : conditions.entrySet()) {
            conditionStr.append(entry.getValue().getColumns())
                    .append(" ")
                    .append(entry.getValue().getComparative())
                    .append(" '")
                    .append(entry.getValue().getValues())
                    .append("' AND ");
        }
        conditionStr.delete(conditionStr.length() - 5, conditionStr.length()); // remove trailing " AND "
        return conditionStr.toString();
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
