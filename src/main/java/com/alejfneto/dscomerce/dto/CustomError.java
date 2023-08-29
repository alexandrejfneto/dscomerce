package com.alejfneto.dscomerce.dto;

import java.time.Instant;

public class CustomError {
	
	private Instant timestamp;
	private Integer status;
	private String error;
	private String path;

	public CustomError(Instant timestamp, Integer status, String error, String path) {
		this.timestamp = timestamp;
		this.status = status;
		this.error = error;
		this.path = path;
	}

	public Instant getTimetamp() {
		return timestamp;
	}

	public Integer getStatus() {
		return status;
	}

	public String getError() {
		return error;
	}

	public String getPath() {
		return path;
	}
		
}
