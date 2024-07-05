package com.sae.service;

import com.sae.dto.SQLQueryRequest;
import com.sae.repository.ExcelSQLGeneratorService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
        for (SQLQueryRequest.SetConditions condition : sqlQueryRequest.getConditions()) {
            StringBuilder sql = new StringBuilder("UPDATE ");
            sql.append(sqlQueryRequest.getTables())
                    .append(" SET ");

            for (SQLQueryRequest.SetValue setValue : sqlQueryRequest.getSetValues()) {
                sql.append(setValue.getColumns())
                        .append(" = '")
                        .append(setValue.getValue())
                        .append("', ");
            }
            sql.delete(sql.length() - 2, sql.length());

            sql.append(" WHERE ")
                    .append(condition.getColumns())
                    .append(" ")
                    .append(condition.getComparative())
                    .append(" '")
                    .append(condition.getValues())
                    .append("'");

            queries.add(sql.toString());
        }
        return queries;
    }

    private List<String> generateDeleteSQL(SQLQueryRequest sqlQueryRequest) {
        List<String> queries = new ArrayList<>();
        for (SQLQueryRequest.SetConditions condition : sqlQueryRequest.getConditions()) {
            StringBuilder sql = new StringBuilder("DELETE FROM ");
            sql.append(sqlQueryRequest.getTables())
                    .append(" WHERE ")
                    .append(condition.getColumns())
                    .append(" ")
                    .append(condition.getComparative())
                    .append(" '")
                    .append(condition.getValues())
                    .append("'");

            queries.add(sql.toString());
        }
        return queries;
    }


    private List<String> generateSelectSQL(SQLQueryRequest sqlQueryRequest) {
        List<String> queries = new ArrayList<>();

        // Iterate over each combination of conditions
        for (int i = 0; i < sqlQueryRequest.getConditions().size(); i += 2) {
            SQLQueryRequest.SetConditions condition1 = sqlQueryRequest.getConditions().get(i);
            SQLQueryRequest.SetConditions condition2 = sqlQueryRequest.getConditions().get(i + 1);

            StringBuilder sql = new StringBuilder("SELECT * FROM ");
            sql.append(sqlQueryRequest.getRegions())
                    .append(".")
                    .append(sqlQueryRequest.getTables())
                    .append(" WHERE ")
                    .append(condition1.getColumns())
                    .append(" ")
                    .append(condition1.getComparative())
                    .append(" '")
                    .append(condition1.getValues())
                    .append("' AND ")
                    .append(condition2.getColumns())
                    .append(" ")
                    .append(condition2.getComparative())
                    .append(" '")
                    .append(condition2.getValues())
                    .append("'");

            // Optionally add set values if needed
            for (SQLQueryRequest.SetValue setValue : sqlQueryRequest.getSetValues()) {
                sql.append(" AND ")
                        .append(setValue.getColumns())
                        .append(" = '")
                        .append(setValue.getValue())
                        .append("'");
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
