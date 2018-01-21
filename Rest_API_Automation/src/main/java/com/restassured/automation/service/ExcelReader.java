package com.restassured.automation.service;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.restassured.automation.model.RowResult;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

public class ExcelReader {

	public ArrayList<RowResult> readExcelData() {

		ArrayList<RowResult> dataRead = new ArrayList<>();
		String filename = "RestAssuredDataRead.xlsx";
		FileInputStream fis = null;

		try {

			fis = new FileInputStream(filename);
			Workbook workbook = null;
			try {
				workbook = WorkbookFactory.create(fis);
			} catch (InvalidFormatException e) {
				e.printStackTrace();
			}
			Sheet sheet = workbook.getSheetAt(0);
			Iterator rowIter = sheet.rowIterator();

			while (rowIter.hasNext()) {
				Row myRow = (Row) rowIter.next();
				if (myRow.getRowNum() == 0) {
					continue; // just skip the rows if row number is 0 or 1
				}
				Iterator cellIter = myRow.cellIterator();
				Vector<String> cellStoreVector = new Vector<String>();
				while (cellIter.hasNext()) {
					DataFormatter formatter = new DataFormatter();
					Cell myCell = (Cell) cellIter.next();
					String cellvalue = formatter.formatCellValue(myCell);
					cellStoreVector.addElement(cellvalue);
				}
				String rowNumber = null;
				String methodType = null;
				String requestURL = null;
				String requestBody = null;
				String fileNameColumn = null;
				String resultPath = null;
				String jsonGeneratedPath = null;
				String apiVersion = null;
				String productName = null;
				String direction = null;

				rowNumber = cellStoreVector.get(0).toString();
				methodType = cellStoreVector.get(1).toString();
				requestURL = cellStoreVector.get(2).toString();
				requestBody = cellStoreVector.get(3).toString();
				fileNameColumn = cellStoreVector.get(4).toString();
				resultPath = cellStoreVector.get(5).toString();
				jsonGeneratedPath = cellStoreVector.get(6).toString();
				apiVersion = cellStoreVector.get(7).toString();
				productName = cellStoreVector.get(8).toString();
				direction = cellStoreVector.get(9).toString();

				dataRead.add(new RowResult(methodType, requestURL, requestBody, fileNameColumn, rowNumber,
						resultPath, jsonGeneratedPath, apiVersion, productName, direction));

			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return dataRead;

	}

}
