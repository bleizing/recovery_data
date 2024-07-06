package com.sae.service;

import com.sae.dto.SQLQueryRequest;
import com.sae.repository.ExcelSQLGeneratorService;

import java.util.*;

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
        System.out.println("size conditionsPerQuery= "+sqlQueryRequest.getConditionsPerQuery());
        int conditionsPerQuery = sqlQueryRequest.getConditionsPerQuery();

        for (int i = 0; i < conditions.size(); i += conditionsPerQuery) {
            StringBuilder sql = new StringBuilder("UPDATE ");
            sql.append(sqlQueryRequest.getRegions())
                    .append(".")
                    .append(sqlQueryRequest.getTables())
                    .append(" SET ");

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
                boolean isNumeric = value.matches("\\d+") && value.length() <= 3;
                if (isNumeric || value.matches(".*\\(.*\\)")){
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
                try {
                    Integer.parseInt(condition.getValues());
                    sql.append(condition.getValues()); // Append without quotes if numeric
                } catch (NumberFormatException e) {
                    sql.append("'").append(condition.getValues()).append("'"); // Append with quotes if not numeric
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
                    boolean isNumeric = value.matches("\\d+") && value.length() <= 3;
                    if (isNumeric || value.matches(".*\\(.*\\)")){
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
}
