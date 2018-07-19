package gov.va.aes.vear.gal.model;

import java.util.ArrayList;
import java.util.List;

public class VearStats {

    List<Person> addedPersons;
    List<Person> updatedPersons;
    List<Person> disabledPersons;
    List<Person> notFoundPersons;
    List<Person> notMappedPersons;
    int vearRecordsRead;
    int vearUpdatedRecords;
    int vearInitialPullRecords;
    int vearPeronsNotFound;
    int vearPersonsNotMapped;
    int vearDisableRecords;

    String runDate;

    public VearStats() {
	super();
	this.disabledPersons = new ArrayList<Person>();
	this.notFoundPersons = new ArrayList<Person>();
	this.notMappedPersons = new ArrayList<Person>();
	this.updatedPersons = new ArrayList<Person>();
	this.addedPersons = new ArrayList<Person>();
    }

    public List<Person> getUpdatedPersons() {
	return updatedPersons;
    }

    public void setUpdatedPersons(List<Person> updatedPersons) {
	this.updatedPersons = updatedPersons;
    }

    public List<Person> getDisabledPersons() {
	return disabledPersons;
    }

    public void setDisabledPersons(List<Person> disabledPersons) {
	this.disabledPersons = disabledPersons;
    }

    public List<Person> getNotFoundPersons() {
	return notFoundPersons;
    }

    public void setNotFoundPersons(List<Person> notFoundPersons) {
	this.notFoundPersons = notFoundPersons;
    }

    public List<Person> getNotMappedPersons() {
	return notMappedPersons;
    }

    public void setNotMappedPersons(List<Person> notMappedPersons) {
	this.notMappedPersons = notMappedPersons;
    }

    public int getVearRecordsRead() {
	return vearRecordsRead;
    }

    public void setVearRecordsRead(int vearRecordsRead) {
	this.vearRecordsRead = vearRecordsRead;
    }

    public int getVearUpdatedRecords() {
	return vearUpdatedRecords;
    }

    public void setVearUpdatedRecords(int vearUpdatedRecords) {
	this.vearUpdatedRecords = vearUpdatedRecords;
    }

    public int getVearInitialPullRecords() {
	return vearInitialPullRecords;
    }

    public void setVearInitialPullRecords(int vearInitialPullRecords) {
	this.vearInitialPullRecords = vearInitialPullRecords;
    }

    public int getVearPeronsNotFound() {
	return vearPeronsNotFound;
    }

    public void setVearPeronsNotFound(int vearPeronsNotFound) {
	this.vearPeronsNotFound = vearPeronsNotFound;
    }

    public int getVearPersonsNotMapped() {
	return vearPersonsNotMapped;
    }

    public void setVearPersonsNotMapped(int vearPersonsNotMapped) {
	this.vearPersonsNotMapped = vearPersonsNotMapped;
    }

    public int getVearDisableRecords() {
	return vearDisableRecords;
    }

    public void setVearDisableRecords(int vearDisableRecords) {
	this.vearDisableRecords = vearDisableRecords;
    }

    public String getRunDate() {
	return runDate;
    }

    public void setRunDate(String date) {
	this.runDate = date;
    }

    public List<Person> getAddedPersons() {
	return addedPersons;
    }

    public void setAddedPersons(List<Person> addedPersons) {
	this.addedPersons = addedPersons;
    }

}
