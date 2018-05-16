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
public class AD_DumpWriter {
	
	public static String FILE_PATH = "." + File.separator + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + File.separator;
	
	public void writeOutput(List<Person> diffUserList) throws IOException {

		String sheetName = "VEAR_GAL_DUMP";// name of sheet

		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet(sheetName);

//		RETURN_ATTRIBUTES = { "userPrincipalName", "sAMAccountName", "givenName",
//				"middleName", "cn", "sn", "mail", "title", "telephoneNumber", "mobile", "streetAddress", "l", "st",
//				"postalCode", "department", "distinguishedName" };
		
		// Add Header Row
		XSSFRow header = sheet.createRow(0);
	
		
		header.createCell(0).setCellValue("User Principal Name");
		header.createCell(1).setCellValue("VA User Name");
		header.createCell(2).setCellValue("Domain");
		
		header.createCell(3).setCellValue("Given Name");
		header.createCell(4).setCellValue("First Name");
		header.createCell(5).setCellValue("Middle Name");
		header.createCell(6).setCellValue("Last Name");
		
		header.createCell(7).setCellValue("Title");
		header.createCell(8).setCellValue("Email");
		
		header.createCell(9).setCellValue("Telephone Number");
		header.createCell(10).setCellValue("Mobile Number");
		header.createCell(11).setCellValue("Street Address");
		header.createCell(12).setCellValue("City");
		header.createCell(13).setCellValue("State");
		header.createCell(14).setCellValue("Zip Code");
		
		header.createCell(15).setCellValue("Department");
		
		applyStyles(header, getHeaderStyle(wb));

		// Write Data
		Person outRecord = null;
		Iterator<Person> outIterator = diffUserList.iterator();
		int rowNumb = 1;
		while (outIterator.hasNext()) {
			outRecord = outIterator.next();
			
			if(outRecord != null) {
				XSSFRow row = sheet.createRow(rowNumb++);
				
				row.createCell(0).setCellValue(cleanUp(outRecord.getUserPrincipalName()));
				row.createCell(1).setCellValue(cleanUp(outRecord.getsAMAccountName()));
				row.createCell(2).setCellValue(cleanUp(outRecord.getDomain()));
				
				row.createCell(3).setCellValue(cleanUp(outRecord.getGivenName()));
				row.createCell(4).setCellValue(cleanUp(outRecord.getFirstName()));
				row.createCell(5).setCellValue(cleanUp(outRecord.getMiddleName()));
				row.createCell(6).setCellValue(cleanUp(outRecord.getLastName()));
				
				row.createCell(7).setCellValue(cleanUp(outRecord.getTitle()));
				row.createCell(8).setCellValue(cleanUp(outRecord.getEmail()));
				
				row.createCell(9).setCellValue(cleanUp(outRecord.getTelephoneNumber()));
				row.createCell(10).setCellValue(cleanUp(outRecord.getMobile()));
				row.createCell(11).setCellValue(cleanUp(outRecord.getStreetAddress()));
				row.createCell(12).setCellValue(cleanUp(outRecord.getCity()));
				row.createCell(13).setCellValue(cleanUp(outRecord.getState()));
				row.createCell(14).setCellValue(cleanUp(outRecord.getPostalCode()));
				
				row.createCell(15).setCellValue(cleanUp(outRecord.getDepartment()));
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
	}

	private String getOutFileName() {
		DateTimeFormatter timeStampPattern = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		String dateStr = timeStampPattern.format(java.time.LocalDateTime.now());
		
		return  FILE_PATH + "VEAR_GAL_DUMP" + dateStr + ".xlsx";
	}
	
	private String cleanUp(String str1) {
        if (str1 == null || str1.trim().isEmpty()) {
        	return "";        	
        }
        return str1;
	}
}
