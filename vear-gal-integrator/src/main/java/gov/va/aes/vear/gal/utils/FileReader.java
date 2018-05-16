package gov.va.aes.vear.gal.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import gov.va.aes.vear.gal.model.Person;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class FileReader {

    private static final String FILE_NAME = "C:/Development/docs/gal_integrator_files/VEAR_GAL_DUMP20180510102407.xlsx";
    		//VEAR_GAL_DUMP20180509152716.xlsx";
    //VEAR_GAL_STAKEHOLDER_DATA
    private static final String FILE_NAME_FOR_DB_RECORDS = "C:/Development/docs/gal_integrator_files/VEAR_GAL_STAKEHOLDER_DATA.xlsx";

    public List<Person> loadADRecordsFromFile() {

        try {

            FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();
            DataFormatter df = new DataFormatter();

           
           
            
            List<Person> personList = new ArrayList<>();
            Row currentRow = iterator.next();
            while (iterator.hasNext()) {

            	
                 
                currentRow = iterator.next();
                Person person = new Person();
                
                if (!df.formatCellValue(currentRow.getCell(0)).trim().isEmpty())
                	person.setUserPrincipalName(df.formatCellValue(currentRow.getCell(0)).trim());
                if (!df.formatCellValue(currentRow.getCell(1)).trim().isEmpty())
                	person.setsAMAccountName(df.formatCellValue(currentRow.getCell(1)).trim());
                if (!df.formatCellValue(currentRow.getCell(2)).trim().isEmpty())
                	person.setDomain(df.formatCellValue(currentRow.getCell(2)).trim());
                if (!df.formatCellValue(currentRow.getCell(3)).trim().isEmpty())
                	person.setGivenName(df.formatCellValue(currentRow.getCell(3)).trim());
                if (!df.formatCellValue(currentRow.getCell(4)).trim().isEmpty())
                	person.setFirstName(df.formatCellValue(currentRow.getCell(4)).trim());
                if (!df.formatCellValue(currentRow.getCell(5)).trim().isEmpty())
                	person.setMiddleName(df.formatCellValue(currentRow.getCell(5)).trim());
                if (!df.formatCellValue(currentRow.getCell(6)).trim().isEmpty())
                	person.setLastName(df.formatCellValue(currentRow.getCell(6)).trim());
                if (!df.formatCellValue(currentRow.getCell(7)).trim().isEmpty())
                	person.setTitle(df.formatCellValue(currentRow.getCell(7)).trim());
                if (!df.formatCellValue(currentRow.getCell(8)).trim().isEmpty())
                	person.setEmail(df.formatCellValue(currentRow.getCell(8)).trim());
                if (!df.formatCellValue(currentRow.getCell(9)).trim().isEmpty())
                	person.setTelephoneNumber(df.formatCellValue(currentRow.getCell(9)).trim());
                if (!df.formatCellValue(currentRow.getCell(10)).trim().isEmpty())
                	person.setMobile(df.formatCellValue(currentRow.getCell(10)).trim());
                if (!df.formatCellValue(currentRow.getCell(11)).trim().isEmpty())
                	person.setStreetAddress(df.formatCellValue(currentRow.getCell(11)).trim());
                if (!df.formatCellValue(currentRow.getCell(12)).trim().isEmpty())
                	person.setCity(df.formatCellValue(currentRow.getCell(12)).trim());
                if (!df.formatCellValue(currentRow.getCell(13)).trim().isEmpty())
                	person.setState(df.formatCellValue(currentRow.getCell(13)).trim());
                if (!df.formatCellValue(currentRow.getCell(14)).trim().isEmpty())
                	person.setPostalCode(df.formatCellValue(currentRow.getCell(14)).trim());
                if (!df.formatCellValue(currentRow.getCell(15)).trim().isEmpty())
                	person.setDepartment(df.formatCellValue(currentRow.getCell(15)).trim());
                
                personList.add(person);
            }
            
            workbook.close();
            return personList;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
    
    public List<Person> loadDBRecordsFromFile() {

        try {

            FileInputStream excelFile = new FileInputStream(new File(FILE_NAME_FOR_DB_RECORDS));
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();
            DataFormatter df = new DataFormatter();

            
            List<Person> personList = new ArrayList<>();
            while (iterator.hasNext()) {

                Row currentRow = iterator.next();
                Person person = new Person();
                
                if (!df.formatCellValue(currentRow.getCell(0)).trim().isEmpty())
					try {
						person.setElementId(new BigDecimal(df.formatCellValue(currentRow.getCell(0)).trim()));
					} catch (NumberFormatException en) {
//						en.printStackTrace();
					}
                if (!df.formatCellValue(currentRow.getCell(1)).trim().isEmpty())
					try {
						person.setStakeholderId(new BigDecimal(df.formatCellValue(currentRow.getCell(1)).trim()));
					} catch (NumberFormatException ep) {
//						ep.printStackTrace();
					}
                if (!df.formatCellValue(currentRow.getCell(2)).trim().isEmpty())
                	person.setUserPrincipalName(df.formatCellValue(currentRow.getCell(2)).trim());
                if (!df.formatCellValue(currentRow.getCell(3)).trim().isEmpty())
                	person.setsAMAccountName(df.formatCellValue(currentRow.getCell(3)).trim());
                if (!df.formatCellValue(currentRow.getCell(4)).trim().isEmpty())
                	person.setDomain(df.formatCellValue(currentRow.getCell(4)).trim());
                if (!df.formatCellValue(currentRow.getCell(5)).trim().isEmpty())
                	person.setGivenName(df.formatCellValue(currentRow.getCell(5)).trim());
                if (!df.formatCellValue(currentRow.getCell(6)).trim().isEmpty())
                	person.setFirstName(df.formatCellValue(currentRow.getCell(6)).trim());
                if (!df.formatCellValue(currentRow.getCell(7)).trim().isEmpty())
                	person.setMiddleName(df.formatCellValue(currentRow.getCell(7)).trim());
                if (!df.formatCellValue(currentRow.getCell(8)).trim().isEmpty())
                	person.setLastName(df.formatCellValue(currentRow.getCell(8)).trim());
                if (!df.formatCellValue(currentRow.getCell(9)).trim().isEmpty())
                	person.setTitle(df.formatCellValue(currentRow.getCell(9)).trim());
                if (!df.formatCellValue(currentRow.getCell(10)).trim().isEmpty())
                	person.setEmail(df.formatCellValue(currentRow.getCell(10)).trim());
                if (!df.formatCellValue(currentRow.getCell(11)).trim().isEmpty())
                	person.setTelephoneNumber(df.formatCellValue(currentRow.getCell(11)).trim());
                if (!df.formatCellValue(currentRow.getCell(12)).trim().isEmpty())
                	person.setMobile(df.formatCellValue(currentRow.getCell(12)).trim());
                if (!df.formatCellValue(currentRow.getCell(13)).trim().isEmpty())
                	person.setStreetAddress(df.formatCellValue(currentRow.getCell(13)).trim());
                if (!df.formatCellValue(currentRow.getCell(14)).trim().isEmpty())
                	person.setCity(df.formatCellValue(currentRow.getCell(14)).trim());
                if (!df.formatCellValue(currentRow.getCell(15)).trim().isEmpty())
                	person.setState(df.formatCellValue(currentRow.getCell(15)).trim());
                if (!df.formatCellValue(currentRow.getCell(16)).trim().isEmpty())
                	person.setPostalCode(df.formatCellValue(currentRow.getCell(16)).trim());
                if (!df.formatCellValue(currentRow.getCell(17)).trim().isEmpty())
                	person.setDepartment(df.formatCellValue(currentRow.getCell(17)).trim());
                
                personList.add(person);
            }
            
            workbook.close();
            return personList;
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}