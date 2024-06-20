package com.bleizing.recovery.dto.model;

import java.io.Serializable;
import java.util.List;

public class OperationDto extends BaseReqDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7776209122480963424L;
	
	private String schema;
	private List<TableDto> tables;
	
	public String getSchema() {
		return schema;
	}
	
	public void setSchema(String schema) {
		this.schema = schema;
	}
	
	public List<TableDto> getTables() {
		return tables;
	}
	
	public void setTables(List<TableDto> tables) {
		this.tables = tables;
	}
}

