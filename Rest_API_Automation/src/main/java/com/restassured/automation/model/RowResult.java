package com.restassured.automation.model;

public class RowResult {

	private String method;
	private String url;
	private String jsonBody;
	private String fileNameColumn;
	private String rowNumber;
	private String resultPath;
	private String jsonGeneratedPath;
	private String apiVersion;
	private String productName;
	private String direction;

	
	public RowResult() {
	}

	public RowResult(String method, String url, String jsonBody, String fileNameColumn,
			String rowNumber, String resultPath, String jsonGeneratedPath, String apiVersion, String productName, String direction) {
		this.method = method;
		this.url = url;
		this.jsonBody = jsonBody;
		this.fileNameColumn = fileNameColumn;
		this.rowNumber = rowNumber;
		this.resultPath = resultPath;
		this.jsonGeneratedPath = jsonGeneratedPath;
		this.apiVersion = apiVersion;
		this.productName = productName;
		this.direction = direction;
	}
	

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getJsonBody() {
		return jsonBody;
	}

	public void setJsonBody(String jsonBody) {
		this.jsonBody = jsonBody;
	}

	public String getFileNameColumn() {
		return fileNameColumn;
	}

	public void setFileNameColumn(String fileNameColumn) {
		this.fileNameColumn = fileNameColumn;
	}

	public String getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(String rowNumber) {
		this.rowNumber = rowNumber;
	}

	public String getResultPath() {
		return resultPath;
	}

	public void setResultPath(String resultPath) {
		this.resultPath = resultPath;
	}
	
	public String getJsonGeneratedPath() {
		return jsonGeneratedPath;
	}

	public void setJsonGeneratedPath(String jsonGeneratedPath) {
		this.jsonGeneratedPath = jsonGeneratedPath;
	}

	public String getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	

}
