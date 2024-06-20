package com.bleizing.recovery.dto.model;

import java.io.Serializable;
import java.util.List;

public class TableDto extends BaseReqDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1133526684825972789L;
	
	private List<ColumnDto> columns;
	private List<ConditionDto> conditions;
	
	public List<ColumnDto> getColumns() {
		return columns;
	}
	
	public void setColumns(List<ColumnDto> columns) {
		this.columns = columns;
	}

	public List<ConditionDto> getConditions() {
		return conditions;
	}

	public void setConditions(List<ConditionDto> conditions) {
		this.conditions = conditions;
	}
}
