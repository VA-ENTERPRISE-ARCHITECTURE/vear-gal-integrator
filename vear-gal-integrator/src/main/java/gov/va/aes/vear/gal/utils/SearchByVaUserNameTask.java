package gov.va.aes.vear.gal.utils;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

import gov.va.aes.vear.gal.model.Person;
import gov.va.aes.vear.gal.repositories.ADPersonRepository;

public class SearchByVaUserNameTask implements Task<Person, ADPersonRepository> {

    private static final Logger LOG = Logger.getLogger(SearchByVaUserNameTask.class.getName());

    Person vaUser;

    public SearchByVaUserNameTask(Person input) {
	this.vaUser = input;
    }

    @Override
    public Person process(ADPersonRepository adRepo, CountDownLatch latch) throws Exception {
	latch.countDown();
	LOG.log(Level.INFO, "Searching for sAMAccountName: " + vaUser.getVaUserName());

	Person personBySamAccountName;
	try {
	    personBySamAccountName = adRepo.getPersonById(vaUser.getVaUserName());
	} catch (NullPointerException e) {
	    return null;// LDAP search fails with exception no changes to db record
	}

	// try {
	// personBySamAccountName = adRepo.getPersonById(vaUser.getVaUserName());
	// } catch (Exception e) {
	// e.printStackTrace();
	// LOG.log(Level.SEVERE, "Errored-out with AD search", e);
	// return null;// LDAP search fails with exception no changes to db record
	// }

	if (personBySamAccountName == null) {
	    personBySamAccountName = vaUser;
	    personBySamAccountName.setDistinguishedName("NOT_FOUND_IN_AD");
	}
	personBySamAccountName.setElementId(vaUser.getElementId());
	personBySamAccountName.setStakeholderId(vaUser.getStakeholderId());

	return personBySamAccountName;
    }

}
