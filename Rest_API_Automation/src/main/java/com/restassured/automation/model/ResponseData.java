package com.restassured.automation.model;

/**
 * ResponseData Class for Transformation Micro Service
 * 
 * @author ryan.dsouza
 *
 */
public class ResponseData {

	private String data;
	private String errorMessage;
	private String status;

	public ResponseData(String data, String errorMessage, String status) {
		super();
		this.data = data;
		this.errorMessage = errorMessage;
		this.status = status;
	}

	public ResponseData() {
		super();
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ResponseData [data=" + data + ", errorMessage=" + errorMessage + ", status=" + status + "]";
	}

}
