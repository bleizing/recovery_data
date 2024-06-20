package com.bleizing.recovery.dto.response;

import java.io.Serializable;

public class UploadResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6070070420924265980L;
	
	private String message;
	private String fileDownloadUri;
	
	public UploadResponse(String message, String fileDownloadUri) {
		super();
		this.message = message;
		this.fileDownloadUri = fileDownloadUri;
	}

	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getFileDownloadUri() {
		return fileDownloadUri;
	}
	
	public void setFileDownloadUri(String fileDownloadUri) {
		this.fileDownloadUri = fileDownloadUri;
	}
}
