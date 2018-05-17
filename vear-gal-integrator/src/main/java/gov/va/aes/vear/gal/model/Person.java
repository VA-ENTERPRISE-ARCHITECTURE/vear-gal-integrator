package gov.va.aes.vear.gal.model;

import java.math.BigDecimal;

public class Person {
	
	private	BigDecimal	stakeholderId;
	private	BigDecimal	elementId;
	public String userPrincipalName;
	public String sAMAccountName;
	public String givenName; //cn
	public String middleName;
	public String firstName; //givenName
	public String lastName; //sn
	public String email; //mail
	public String title;
	public String telephoneNumber;
	public String mobile;
	public String streetAddress;
	public String city; //l
	public String state; //st
	public String postalCode;
	public String department;
	public String domain;
	
	public Person() {
		super();
	
	}
	
	public Person(BigDecimal elementId, BigDecimal stakeholderId) {
		super();
		this.elementId = elementId;
		this.stakeholderId = stakeholderId;
	}
	
	public BigDecimal getStakeholderId() {
		return stakeholderId;
	}
	public void setStakeholderId(BigDecimal stakeholderId) {
		this.stakeholderId = stakeholderId;
	}
	public BigDecimal getElementId() {
		return elementId;
	}
	public void setElementId(BigDecimal elementId) {
		this.elementId = elementId;
	}
	public String getUserPrincipalName() {
		return userPrincipalName;
	}
	public void setUserPrincipalName(String userPrincipalName) {
		this.userPrincipalName = userPrincipalName;
	}
	public String getsAMAccountName() {
		return sAMAccountName;
	}
	public void setsAMAccountName(String sAMAccountName) {
		this.sAMAccountName = sAMAccountName;
	}
	public String getGivenName() {
		return givenName;
	}
	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTelephoneNumber() {
		return telephoneNumber;
	}
	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getStreetAddress() {
		return streetAddress;
	}
	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}

	
}
