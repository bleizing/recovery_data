package com.sae.mapper;

import com.sae.models.request.SQLRequest;
import com.sae.entity.SetConditions;
import com.sae.entity.Requests;
import com.sae.entity.SetValue;
import com.sae.entity.Users;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SQLQueryRequestMapper {

    public static SQLRequest toDto(Requests requests, Map<String, String> headers) {
        return SQLRequest.builder()
                .fileLocation(requests.getLocationFile())
                .regions(requests.getRegions())
                .tables(requests.getTables())
                .columns(requests.getColumns())
                .setValues(toSetValueDtoList(requests.getSetValue()))
                .conditions(toConditionDtoList(requests.getConditions()))
                .headers(headers.toString())
                .build();
    }

    private static List<SQLRequest.SetConditions> toConditionDtoList(List<SetConditions> conditionsList) {
        return conditionsList.stream()
                .map(conditions -> {
                    SQLRequest.SetConditions setCondition = new SQLRequest.SetConditions();
                    setCondition.setColumns(conditions.getColumns());
                    setCondition.setComparative(conditions.getComparative());
                    setCondition.setValues(conditions.getValues());
                    return setCondition;
                })
                .collect(Collectors.toList());
    }

    private static List<SQLRequest.SetValue> toSetValueDtoList(List<SetValue> settersValList) {
        return settersValList.stream()
                .map(settersVal -> {
                    SQLRequest.SetValue setValue = new SQLRequest.SetValue();
                    setValue.setColumns(settersVal.getColumns());
                    setValue.setValue(settersVal.getValues());
                    return setValue;
                })
                .collect(Collectors.toList());
    }

    public static Requests toEntity(SQLRequest sqlQueryRequest, Users user) {
        Requests requests = new Requests();
        requests.setUsers(user);
        requests.setRegions(sqlQueryRequest.getRegions());
        requests.setTables(sqlQueryRequest.getTables());
        requests.setColumns(sqlQueryRequest.getColumns());
        requests.setSetValue(toSettersValEntityList(sqlQueryRequest.getSetValues(), requests));
        requests.setConditions(toConditionsEntityList(sqlQueryRequest.getConditions(), requests));
        return requests;
    }

    private static List<SetValue> toSettersValEntityList(List<SQLRequest.SetValue> setValueList, Requests requests) {
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
    private static List<SetConditions> toConditionsEntityList(List<SQLRequest.SetConditions> setConditionList, Requests requests) {
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


