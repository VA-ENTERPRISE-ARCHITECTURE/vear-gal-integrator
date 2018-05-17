package gov.va.aes.vear.gal.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import gov.va.aes.vear.gal.model.Person;




@Component
public class ADRecordsWriter {
	
	public static String FILE_PATH = "." + File.separator + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + File.separator;
	
	public void writeOutput(List<Person> diffUserList) throws IOException {

		String sheetName = "VEAR_GAL_DATA";// name of sheet

		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet(sheetName);

		// Add Header Row
		XSSFRow header = sheet.createRow(0);
		header.createCell(0).setCellValue("Element Id");
		header.createCell(1).setCellValue("Stakeholder Id");
		
		header.createCell(2).setCellValue("User Principal Name");
		header.createCell(3).setCellValue("VA User Name");
		header.createCell(4).setCellValue("Domain");
		
		header.createCell(5).setCellValue("Given Name");
		header.createCell(6).setCellValue("First Name");
		header.createCell(7).setCellValue("Middle Name");
		header.createCell(8).setCellValue("Last Name");
		
		header.createCell(9).setCellValue("Title");
		header.createCell(10).setCellValue("Email");
		
		header.createCell(11).setCellValue("Telephone Number");
		header.createCell(12).setCellValue("Mobile Number");
		header.createCell(13).setCellValue("Street Address");
		header.createCell(14).setCellValue("City");
		header.createCell(15).setCellValue("State");
		header.createCell(16).setCellValue("Zip Code");
		
		header.createCell(17).setCellValue("Department");
		
		applyStyles(header, getHeaderStyle(wb));

		// Write Data
		Person outRecord = null;
		Iterator<Person> outIterator = diffUserList.iterator();
		int rowNumb = 1;
		
		while (outIterator.hasNext()) {
			outRecord = outIterator.next();
			if(outRecord != null) {
				XSSFRow row = sheet.createRow(rowNumb++);
				row.createCell(0).setCellValue(outRecord.getElementId().toPlainString());
				row.createCell(1).setCellValue(outRecord.getStakeholderId().toPlainString());
	
				row.createCell(2).setCellValue(outRecord.getUserPrincipalName());
				row.createCell(3).setCellValue(outRecord.getsAMAccountName());
				row.createCell(4).setCellValue(outRecord.getDomain());
				
				row.createCell(5).setCellValue(outRecord.getGivenName());
				row.createCell(6).setCellValue(outRecord.getFirstName());
				row.createCell(7).setCellValue(outRecord.getMiddleName());
				row.createCell(8).setCellValue(outRecord.getLastName());
				
				row.createCell(9).setCellValue(outRecord.getTitle());
				row.createCell(10).setCellValue(outRecord.getEmail());
				
				row.createCell(11).setCellValue(outRecord.getTelephoneNumber());
				row.createCell(12).setCellValue(outRecord.getMobile());
				row.createCell(13).setCellValue(outRecord.getStreetAddress());
				row.createCell(14).setCellValue(outRecord.getCity());
				row.createCell(15).setCellValue(outRecord.getState());
				row.createCell(16).setCellValue(outRecord.getPostalCode());
				
				row.createCell(17).setCellValue(outRecord.getDepartment());
			}
		}
		// write this workbook to an Output stream.
		File file = new File(getOutFileName());
		file.getParentFile().mkdirs();
		FileOutputStream fileOut = new FileOutputStream(file);

		wb.write(fileOut);
		fileOut.flush();
		fileOut.close();
	}

	private CellStyle getHeaderStyle(XSSFWorkbook wb) {
		XSSFFont font = wb.createFont();
		font.setFontHeightInPoints((short) 12);
		font.setFontName("Calibri");
		font.setColor(IndexedColors.BLACK.getIndex());
		font.setBold(false);
		font.setItalic(false);

		CellStyle style = wb.createCellStyle();
		style.setFont(font);

		return style;

	}

	private CellStyle getSubHeaderStyle(XSSFWorkbook wb) {
		XSSFFont font = wb.createFont();
		font.setFontHeightInPoints((short) 12);
		font.setFontName("Calibri");
		font.setColor(IndexedColors.BLUE.getIndex());
		font.setBold(true);
		font.setItalic(false);

		CellStyle style = wb.createCellStyle();
		style.setFont(font);

		return style;
	}

	private void applyStyles(XSSFRow row, CellStyle style) {
		row.getCell(0).setCellStyle(style);
		row.getCell(1).setCellStyle(style);
		row.getCell(2).setCellStyle(style);
		row.getCell(3).setCellStyle(style);
		row.getCell(4).setCellStyle(style);
		row.getCell(5).setCellStyle(style);
		row.getCell(6).setCellStyle(style);
		row.getCell(7).setCellStyle(style);
		row.getCell(8).setCellStyle(style);
		row.getCell(9).setCellStyle(style);
		row.getCell(10).setCellStyle(style);
		row.getCell(11).setCellStyle(style);
		row.getCell(12).setCellStyle(style);
		row.getCell(13).setCellStyle(style);
		row.getCell(14).setCellStyle(style);
		row.getCell(15).setCellStyle(style);
		row.getCell(16).setCellStyle(style);
		row.getCell(17).setCellStyle(style);
	}

	private String getOutFileName() {
		DateTimeFormatter timeStampPattern = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		String dateStr = timeStampPattern.format(java.time.LocalDateTime.now());
		
		return  FILE_PATH + "VEAR_GAL_UPDATE" + dateStr + ".xlsx";
	}
}
