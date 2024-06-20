package com.bleizing.recovery.dto.model;

import java.io.Serializable;

public class BaseReqDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5709925659489385375L;
	
	private String name;
	private String value;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
