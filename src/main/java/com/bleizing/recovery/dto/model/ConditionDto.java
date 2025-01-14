package com.bleizing.recovery.dto.model;

import java.io.Serializable;

public class ConditionDto extends BaseReqDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2220499595640454153L;
	
	private boolean number = false;

	public boolean isNumber() {
		return number;
	}

	public void setNumber(boolean number) {
		this.number = number;
	}
}
