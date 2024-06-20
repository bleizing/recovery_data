package com.bleizing.recovery.dto.model;

import java.util.HashMap;
import java.util.List;

public class GenerateSqlDto {
	private int rowTotal;
	private HashMap<Integer, List<ColumnDto>> columnsMap;
	private HashMap<Integer, List<ConditionDto>> conditionsMap;
	
	public int getRowTotal() {
		return rowTotal;
	}

	public void setRowTotal(int rowTotal) {
		this.rowTotal = rowTotal;
	}

	public HashMap<Integer, List<ColumnDto>> getColumnsMap() {
		return columnsMap;
	}

	public void setColumnsMap(HashMap<Integer, List<ColumnDto>> columnsMap) {
		this.columnsMap = columnsMap;
	}

	public HashMap<Integer, List<ConditionDto>> getConditionsMap() {
		return conditionsMap;
	}

	public void setConditionsMap(HashMap<Integer, List<ConditionDto>> conditionsMap) {
		this.conditionsMap = conditionsMap;
	}
}
