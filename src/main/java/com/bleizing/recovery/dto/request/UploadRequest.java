package com.bleizing.recovery.dto.request;

import java.io.Serializable;
import java.util.List;

import com.bleizing.recovery.dto.model.BaseReqDto;
import com.bleizing.recovery.dto.model.OperationDto;

public class UploadRequest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3526440441576829419L;
	
	private List<OperationDto> operations;

	public List<OperationDto> getOperations() {
		return operations;
	}

	public void setOperations(List<OperationDto> operations) {
		this.operations = operations;
	}
}
