package gov.va.aes.vear.gal.utils;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

import gov.va.aes.vear.gal.model.Person;
import gov.va.aes.vear.gal.repositories.ADPersonRepository;

public class SearchByEmailTask implements Task<Person, ADPersonRepository> {

    private static final Logger LOG = Logger.getLogger(SearchByEmailTask.class.getName());

    Person vaUser;

    public SearchByEmailTask(Person input) {
	this.vaUser = input;
    }

    @Override
    public Person process(ADPersonRepository adRepo, CountDownLatch latch) throws Exception {
	latch.countDown();
	LOG.log(Level.INFO, "Searching for email: " + vaUser.getEmail());

	Person personByEmail;
	try {
	    personByEmail = adRepo.getPersonByEmail(vaUser.getEmail());
	} catch (NullPointerException e) {
	    return null;// LDAP search fails with exception no changes to db record
	}

	// try {
	// personByEmail = adRepo.getPersonByEmail(vaUser.getEmail());
	// } catch (Exception e) {
	// e.printStackTrace();
	// LOG.log(Level.SEVERE, "Errored-out with AD search", e);
	// return null;// LDAP search fails with exception no changes to db record
	// }
	if (personByEmail == null) {
	    personByEmail = vaUser;
	    personByEmail.setDistinguishedName("NOT_FOUND_IN_AD");
	}
	personByEmail.setElementId(vaUser.getElementId());
	personByEmail.setStakeholderId(vaUser.getStakeholderId());

	return personByEmail;
    }

}
