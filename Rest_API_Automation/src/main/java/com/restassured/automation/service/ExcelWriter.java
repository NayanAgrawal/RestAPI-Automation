package com.restassured.automation.service;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class ExcelWriter {

	public void WritertoColoumn() {
		try {
			// Get the excel file.
			FileInputStream file = new FileInputStream(
					new File("RestAssuredDataRead_Result.xlsx"));

			// Get workbook for XLS file.
			HSSFWorkbook yourworkbook = new HSSFWorkbook(file);

			// Get first sheet from the workbook.
			// If there have >1 sheet in your workbook, you can change it here
			// IF you want to edit other sheets.
			HSSFSheet sheet1 = yourworkbook.getSheetAt(0);

			// Get the row of your desired cell.
			// Let's say that your desired cell is at row 2.
			Row row = sheet1.getRow(1);
			// Get the column of your desired cell in your selected row.
			// Let's say that your desired cell is at column 2.
			Cell column = row.getCell(1);
			// If the cell is String type.If double or else you can change it.
			String updatename = column.getStringCellValue();
			// New content for desired cell.
			updatename = "Lala";
			// Print out the updated content.
			System.out.println(updatename);
			// Set the new content to your desired cell(column).
			column.setCellValue(updatename);
			// Close the excel file.
			file.close();
			// Where you want to save the updated sheet.
			FileOutputStream out = new FileOutputStream(new File("RestAssuredDataRead_Result.xlsx"));
			yourworkbook.write(out);
			out.close();

		} catch (FileNotFoundException e11) {
			e11.printStackTrace();
		} catch (IOException e12) {
			e12.printStackTrace();
		}
	}

}
