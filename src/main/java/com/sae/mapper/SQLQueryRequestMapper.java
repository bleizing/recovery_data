package com.sae.mapper;

import com.sae.models.request.SQLQueryRequest;
import com.sae.entity.SetConditions;
import com.sae.entity.Requests;
import com.sae.entity.SetValue;
import com.sae.entity.Users;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SQLQueryRequestMapper {

    public static SQLQueryRequest toDto(Requests requests, Map<String, String> headers) {
        return SQLQueryRequest.builder()
                .fileLocation(requests.getLocationFile())
                .regions(requests.getRegions())
                .tables(requests.getTables())
                .columns(requests.getColumns())
                .setValues(toSetValueDtoList(requests.getSetValue()))
                .conditions(toConditionDtoList(requests.getConditions()))
                .headers(headers.toString())
                .build();
    }

    private static List<SQLQueryRequest.SetConditions> toConditionDtoList(List<SetConditions> conditionsList) {
        return conditionsList.stream()
                .map(conditions -> {
                    SQLQueryRequest.SetConditions setCondition = new SQLQueryRequest.SetConditions();
                    setCondition.setColumns(conditions.getColumns());
                    setCondition.setComparative(conditions.getComparative());
                    setCondition.setValues(conditions.getValues());
                    return setCondition;
                })
                .collect(Collectors.toList());
    }

    private static List<SQLQueryRequest.SetValue> toSetValueDtoList(List<SetValue> settersValList) {
        return settersValList.stream()
                .map(settersVal -> {
                    SQLQueryRequest.SetValue setValue = new SQLQueryRequest.SetValue();
                    setValue.setColumns(settersVal.getColumns());
                    setValue.setValue(settersVal.getValues());
                    return setValue;
                })
                .collect(Collectors.toList());
    }

    public static Requests toEntity(SQLQueryRequest sqlQueryRequest, Users user) {
        Requests requests = new Requests();
        requests.setUsers(user);
        requests.setRegions(sqlQueryRequest.getRegions());
        requests.setTables(sqlQueryRequest.getTables());
        requests.setColumns(sqlQueryRequest.getColumns());
        requests.setSetValue(toSettersValEntityList(sqlQueryRequest.getSetValues(), requests));
        requests.setConditions(toConditionsEntityList(sqlQueryRequest.getConditions(), requests));
        return requests;
    }

    private static List<SetValue> toSettersValEntityList(List<SQLQueryRequest.SetValue> setValueList, Requests requests) {
        return setValueList.stream()
                .map(setValue -> {
                    SetValue settersVal = new SetValue();
                    settersVal.setRequests(requests);
                    settersVal.setColumns(setValue.getColumns());
                    settersVal.setValues(setValue.getValue());
                    return settersVal;
                })
                .collect(Collectors.toList());
    }
    private static List<SetConditions> toConditionsEntityList(List<SQLQueryRequest.SetConditions> setConditionList, Requests requests) {
        return setConditionList.stream()
                .map(setCondition -> {
                    SetConditions conditions = new SetConditions();
                    conditions.setRequests(requests);
                    conditions.setColumns(setCondition.getColumns());
                    conditions.setComparative(setCondition.getComparative());
                    conditions.setValues(setCondition.getValues());
                    return conditions;
                })
                .collect(Collectors.toList());
    }
}


