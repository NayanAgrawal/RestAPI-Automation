package com.zycus.main;

import static com.jayway.restassured.RestAssured.given;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import org.xml.sax.SAXException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ValidatableResponse;
import com.restassured.automation.model.ResponseData;
import com.restassured.automation.model.RowResult;
import com.restassured.automation.service.ExcelReader;
import com.restassured.automation.service.ReadPropertiesFile;

public class RestAssured {
	private ArrayList<RowResult> excelData;
	private Map<String, String> headers;
	PrintWriter out = null;
	DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
	Date date = new Date();
	File created_JSON_file = null;
	File created_XML_file = null;
	File created_Result_file = null;
	BufferedWriter bw = null;
	FileWriter fw = null;
	Gson gson = new GsonBuilder().setPrettyPrinting().create();
	JsonParser jp = new JsonParser();
	Exception exception = null;
	List<String> data = null;
	private XSSFWorkbook workbook;
	long start;
	long responseTime;
	ValidatableResponse response;

	private final String SHEET_NAME = "API Info";

	private ArrayList<String[]> allData;

	@BeforeTest
	public void initializeData() {
		ExcelReader reader = new ExcelReader();
		excelData = reader.readExcelData();
		headers = new HashMap<>();
		data = new ArrayList<>();
		allData = new ArrayList<>();
		String[] columnHeaders = { "S.No", "Execution Date", "METHOD", "Product", "API URL", "version", "Test_Type",
				"Response_code", "Response_Time", "Request_Body_path", "Resopne_JSON_FILENAME", "STATUS" };
		allData.add(columnHeaders);

		Properties pro = new Properties();
		String keyValues = null;

		try {
			FileInputStream in = new FileInputStream("constant.properties");
			pro.load(in);
			System.out.println("All keys of the property file : ");
			System.out.println(pro.keySet());
			Enumeration em = pro.keys();
			while (em.hasMoreElements()) {

				keyValues = (String) em.nextElement();
				System.out.println(keyValues + "=" + pro.get(keyValues));
				headers.put(keyValues, (String) pro.get(keyValues));
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("______" + headers);
		System.out.println("heeaders - " + keyValues + "=" + pro.get(keyValues));

		//headers.put("Content-Type", ReadPropertiesFile.getProperty("Content-Type"));
	}

	private String readContent(RowResult r) throws IOException {
		String sCurrentLine;
		String file = "";
		BufferedReader br = new BufferedReader(new FileReader(r.getJsonBody()));
		while ((sCurrentLine = br.readLine()) != null) {
			file += sCurrentLine;
		}
		br.close();
		return file;
	}

	public boolean validateXMLAgainstXSD(String xml, String xsd) throws IOException {
		Source xmlToBeValidated = new StreamSource(new StringReader(xml));
		Source xsdToBeValidatedSAgainst = new StreamSource(new StringReader(xsd));

		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		try {
			Schema schema = factory.newSchema(xsdToBeValidatedSAgainst);
			Validator validator = schema.newValidator();
			validator.validate(xmlToBeValidated);
		} catch (IOException | SAXException e) {
			exception = e;
			System.out.println("SAX Exception: " + e.getMessage());
			return false;
		}
		return true;
	}

	public void POSTResponse(RowResult r) throws IOException {
		ResponseData requestValidationData = new ResponseData();
		ResponseData responseValidationData = new ResponseData();
		String file = readContent(r);
		start = System.currentTimeMillis();
		Response response = given().contentType(ContentType.JSON).headers(headers).body(file).when()
				.post(new URL(r.getUrl()));
		responseTime = System.currentTimeMillis() - start;
		System.out.println("Response Time : " + responseTime);
		System.out.println("Response:" + response.getStatusCode());

		if (response.getStatusCode() == 200) {
			created_JSON_file = new File(r.getJsonGeneratedPath() + "POST_" + r.getFileNameColumn() + "_"
					+ dateFormat.format(date) + "_" + r.getRowNumber() + ".json");
			JsonElement je = jp.parse(response.body().asString());
			String prettyJsonString = gson.toJson(je);
			data.add(prettyJsonString);
			System.out.println(prettyJsonString);
			writeToFile(r.getJsonGeneratedPath() + "POST_" + r.getFileNameColumn() + "_" + dateFormat.format(date) + "_"
					+ r.getRowNumber() + ".json", data);

			validate(requestValidationData, responseValidationData, response, r);
		} else {
			String[] columnData;
			created_JSON_file = new File(r.getJsonGeneratedPath() + "POST_" + r.getFileNameColumn() + "_"
					+ dateFormat.format(date) + "_" + r.getRowNumber() + ".json");
			columnData = new String[] { r.getRowNumber(), dateFormat.format(date), r.getMethod(), r.getProductName(),
					r.getUrl(), r.getApiVersion(), r.getDirection(), "" + response.getStatusCode(),
					String.valueOf(responseTime), r.getJsonBody(), created_JSON_file.getName(), "", "" };
			columnData[12] = "FAILED : " + response.body().asString();
			columnData[11] = "FAILED : " + response.body().asString();
			allData.add(columnData);
		}

	}

	private void validate(ResponseData requestValidationData, ResponseData responseValidationData, Response apiResponse,
			RowResult r) throws IOException {

		String[] columnData;
		columnData = new String[] { r.getRowNumber(), dateFormat.format(date), r.getMethod(), r.getProductName(),
				r.getUrl(), r.getApiVersion(), r.getDirection(), "" + apiResponse.getStatusCode(),
				String.valueOf(responseTime), r.getJsonBody(), created_JSON_file.getName(), "", "" };
		if (requestValidationData != null && requestValidationData.getData().equalsIgnoreCase("true")) {
			System.out.println("Successfully validated Request XML against XSD");
			columnData[12] = "SUCCESS";

		} else {
			System.out.println("Unable to validate Request XML against XSD");
			columnData[12] = "FAILED : " + requestValidationData.getErrorMessage();
		}

		if (responseValidationData.getData().equalsIgnoreCase("true")) {
			System.out.println("Successfully validated Response XML against XSD");
			columnData[11] = "SUCCESS";
		} else {
			System.out.println("Unable to validate Response XML against XSD");
			columnData[11] = "FAILED : " + responseValidationData.getErrorMessage();

		}
		allData.add(columnData);

	}

	public void GETResponse(RowResult r) throws IOException {

		start = System.currentTimeMillis();
		System.out.println("Headers are " + headers);
		System.out.println("start time :" + start);
		response = given().headers(headers).when().get(r.getUrl()).then();
		responseTime = System.currentTimeMillis() - start;
		System.out.println("Response Time : " + String.valueOf(responseTime));
		System.out.println("Response:" + response.extract().statusCode());
		System.out.println(response.extract().body().asString());

		if (response.extract().statusCode() == 200) {
			created_JSON_file = new File(r.getJsonGeneratedPath() + "GET_" + r.getFileNameColumn() + "_"
					+ dateFormat.format(date) + "_" + r.getRowNumber() + ".json");

			// Format Generated JSON
			JsonElement je = jp.parse(response.extract().body().asString());
			String prettyJsonString = gson.toJson(je);
			data.add(prettyJsonString);
			writeToFile(r.getJsonGeneratedPath() + "GET_" + r.getFileNameColumn() + "_" + dateFormat.format(date) + "_"
					+ r.getRowNumber() + ".json", data);

			validateResponse(response, r);

		} else {
			String[] columnData;
			columnData = new String[] { r.getRowNumber(), dateFormat.format(date), r.getMethod(), r.getProductName(),
					r.getUrl(), r.getApiVersion(), r.getDirection(), "" + response.extract().statusCode(),
					String.valueOf(responseTime), r.getJsonBody(), "Failed", created_JSON_file.getName(), "", "" };
			columnData[11] = "FAILED : " + response.extract().body().asString();
			columnData[10] = "Status Code is " + response.extract().statusCode() + ", file not created";
			allData.add(columnData);
		}

	}

	private void validateResponse(ValidatableResponse apiResponse, RowResult r) throws IOException {

		String[] columnData;
		columnData = new String[] { r.getRowNumber(), dateFormat.format(date), r.getMethod(), r.getProductName(),
				r.getUrl(), r.getApiVersion(), r.getDirection(), "" + apiResponse.extract().statusCode(),
				String.valueOf(responseTime), r.getJsonBody(), "", created_JSON_file.getName(), "" };

		columnData[10] = created_JSON_file.getName();
		if (response.extract().statusCode() == 200) {
			columnData[11] = "SUCCESS";
		} else {
			columnData[11] = "FAILED";
		}
		allData.add(columnData);
	}

	public void writeToFile(String path, List<String> data) throws IOException {
		System.out.println("Writing to File: " + path);
		FileUtils.writeLines(new File(path), data);
		data.clear();
	}

	@Test
	public void Response() throws IOException {
		// createOutputFile();
		for (RowResult r : excelData) {
			System.out.println("Row num: " + r.getRowNumber());
			System.out.println(r.getMethod() + " " + r.getUrl());
			System.out.println("Json Body:" + r.getJsonBody());
			String method = r.getMethod();
			if (method.equalsIgnoreCase("GET")) {
				GETResponse(r);
				createOutputFile();
			} else if (method.equalsIgnoreCase("POST")) {
				POSTResponse(r);
				createOutputFile();
			}
			System.out.println("Row num: " + r.getRowNumber());

		}

	}

	private void createOutputFile() {
		workbook = new XSSFWorkbook();
		XSSFSheet spreadsheet = workbook.createSheet(SHEET_NAME);
		for (int i = 0; i < allData.size(); i++) {
			XSSFRow row = spreadsheet.createRow(i);
			int cellid = 0;
			for (String data : allData.get(i)) {
				Cell cell = row.createCell(cellid++);
				cell.setCellValue(data);
			}
		}

		FileOutputStream out;
		try {
			out = new FileOutputStream(new File("resultData_" + dateFormat.format(date) + ".xlsx"));
			workbook.write(out);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Writesheet.xlsx created successfully");

	}

}