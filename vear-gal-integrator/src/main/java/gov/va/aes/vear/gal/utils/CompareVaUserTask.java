package gov.va.aes.vear.gal.utils;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

import gov.va.aes.vear.gal.model.Person;
import gov.va.aes.vear.gal.repositories.ADPersonRepository;

public class CompareVaUserTask implements Task<Person, ADPersonRepository> {

    private static final Logger LOG = Logger.getLogger(CompareVaUserTask.class.getName());

    Person vaUserFromDb;

    public CompareVaUserTask(Person input) {
	this.vaUserFromDb = input;
    }

    // element-id, stakeholder_id, Name, email, phone, title
    @Override
    public Person process(ADPersonRepository adRepo, CountDownLatch latch) throws Exception {
	latch.countDown();

	Person personBySamAccountName = null;

	try {
	    personBySamAccountName = adRepo.getPersonByIdAndDomain(vaUserFromDb.getVaUserName(),
		    vaUserFromDb.getConnectionDomain());
	} catch (NullPointerException e) {
	    return null;// LDAP search fails with exception no changes to db record
	}

	// try {
	// personBySamAccountName =
	// adRepo.getPersonByIdAndDomain(vaUserFromDb.getVaUserName(),
	// vaUserFromDb.getConnectionDomain());
	// } catch (Exception e) {
	// e.printStackTrace();
	// LOG.log(Level.SEVERE, "Errored-out with AD search", e);
	// return null;// LDAP search fails with exception no changes to db record
	// }

	if (personBySamAccountName == null) { // not found in ad
	    LOG.log(Level.INFO, "NOT FOUND in AD sAMAccountName: " + vaUserFromDb.getVaUserName() + " and domain: "
		    + vaUserFromDb.getConnectionDomain());
	    personBySamAccountName = vaUserFromDb;
	    personBySamAccountName.setDistinguishedName("NOT_FOUND_IN_AD");
	} else {
	    if (!checkUserAttributesChanged(vaUserFromDb, personBySamAccountName)) {
		personBySamAccountName = null; // no changes return null
	    }

	}
	if (personBySamAccountName != null) { // ad change found
	    personBySamAccountName.setElementId(vaUserFromDb.getElementId());
	    personBySamAccountName.setStakeholderId(vaUserFromDb.getStakeholderId());
	}
	return personBySamAccountName;
    }

    private boolean checkUserAttributesChanged(Person userFromDb, Person userFromAd) {
	boolean checkUserAttributesChanged = false;
	checkUserAttributesChanged = !compare(userFromDb.getFirstName(), userFromAd.getFirstName())
		|| !compare(userFromDb.getLastName(), userFromAd.getLastName())
		|| !compare(userFromDb.getMiddleName(), userFromAd.getMiddleName())
		|| !compare(userFromDb.getTitle(), userFromAd.getTitle())
		|| !(comparePhone(userFromDb.getTelephoneNumber(), userFromAd.getTelephoneNumber())
			|| comparePhone(userFromDb.getTelephoneNumber(), userFromAd.getMobile()));
	if (checkUserAttributesChanged) {
	    LOG.log(Level.INFO,
		    "Changes Found for " + userFromDb.getVaUserName() + " --> db record: " + userFromDb.getFirstName()
			    + " | " + userFromDb.getLastName() + " | " + userFromDb.getMiddleName() + " | "
			    + userFromDb.getTitle() + " | " + userFromDb.getTelephoneNumber() + ", Ad record: "
			    + userFromAd.getFirstName() + " | " + userFromAd.getLastName() + " | "
			    + userFromAd.getMiddleName() + " | " + userFromAd.getTitle() + " | "
			    + userFromAd.getTelephoneNumber() + " | " + userFromAd.getMobile());
	} else {
	    LOG.log(Level.INFO, "Changes-not Found for " + userFromDb.getVaUserName() + " --> db record: "
		    + userFromDb.getFirstName() + " | " + userFromDb.getLastName() + " | " + userFromDb.getTitle()
		    + " | " + userFromDb.getTelephoneNumber() + ", Ad record: " + userFromAd.getFirstName() + " | "
		    + userFromAd.getLastName() + " | " + userFromAd.getTitle() + " | " + userFromAd.getTelephoneNumber()
		    + " | " + userFromAd.getMobile());

	}
	return checkUserAttributesChanged;
    }

    private boolean comparePhone(String phone1, String phone2) {
	boolean comparePhoneFlag = false;
	phone1 = cleanUp(phone1);
	phone2 = cleanUp(phone2);
	if (phone1 == null) {
	    return phone2 == null;
	} else {
	    if (phone2 == null) {
		return comparePhoneFlag;
	    } else {
		String p1 = phone1.replaceAll("[^\\d]", "");
		String p2 = phone2.replaceAll("[^\\d]", "");
		comparePhoneFlag = (p1.equals(p2) || p2.contains(p1));
		return comparePhoneFlag;
	    }
	}
    }

    public boolean compare(String str1, String str2) {
	boolean compareFlag = false;
	str1 = cleanUp(str1);
	str2 = cleanUp(str2);
	compareFlag = (str1 == null ? str2 == null : str1.equals(str2));
	return compareFlag;
    }

    private String cleanUp(String str1) {
	if (str1 == null || str1.trim().isEmpty()) {
	    return "";
	}
	return str1;
    }

}
