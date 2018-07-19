package gov.va.aes.vear.gal.main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.va.aes.vear.gal.data.StakeholderDAO;
import gov.va.aes.vear.gal.model.Person;
import gov.va.aes.vear.gal.model.VearStats;
import gov.va.aes.vear.gal.repositories.ADPersonRepository;
import gov.va.aes.vear.gal.utils.ADRecordsWriter;
import gov.va.aes.vear.gal.utils.FormatPhoneNumber;
import gov.va.aes.vear.gal.utils.PersonResults;
import gov.va.aes.vear.gal.utils.SearchByVaUserNameTask;
import gov.va.aes.vear.gal.utils.Task;
import gov.va.aes.vear.gal.utils.WaitingFuturesRunner;

@Component
public class AddNewRecordsProcessor {

    private static final Logger LOG = Logger.getLogger(AddNewRecordsProcessor.class.getName());

    @Autowired
    ADPersonRepository repository;
    @Autowired
    StakeholderDAO stakeholderDao;
    @Autowired
    ADRecordsWriter outputWriter;
    @Autowired
    FormatPhoneNumber formatPhone;

    public VearStats process() throws Exception {
	VearStats vearStats = new VearStats();
	String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	vearStats.setRunDate(date);
	long startTime = System.nanoTime();
	long estimatedTime = 0;
	List<Person> adPerons = new ArrayList<Person>();
	List<Person> addedPersons = new ArrayList<Person>();
	List<Person> adPeronsNotFound = new ArrayList<Person>();
	List<Person> adPersonsNotMapped = new ArrayList<Person>();

	List<Person> stakeholders = stakeholderDao.getStakeholdersWithOUTDomain();

	LOG.log(Level.INFO, "Total Records Read from Stakeholder table = " + stakeholders.size());
	vearStats.setVearRecordsRead(stakeholders.size());

	estimatedTime = (System.nanoTime() - startTime) / 1000000;
	LOG.log(Level.FINE, "VEAR DB access Time in Seconds: " + TimeUnit.MILLISECONDS.toSeconds(estimatedTime));

	startTime = System.nanoTime();

	final List<Task<Person, ADPersonRepository>> taskList = setupSearchBySamAccountNameTasks(stakeholders);

	final WaitingFuturesRunner<Person, ADPersonRepository> completableFuturesRunner = new WaitingFuturesRunner<>(
		taskList, 1, 4, TimeUnit.SECONDS);
	final PersonResults consolidatedResults = new PersonResults();

	completableFuturesRunner.go(repository, consolidatedResults);

	adPerons = consolidatedResults.getResults();

	for (Person adPerson : adPerons) {
	    if (adPerson != null) {// adPerson would be null if ldap search fails with exception
		String phone = formatPhone.formatPhone(adPerson.getTelephoneNumber(), adPerson.getMobile());
		adPerson.setTelephoneNumber(phone);
		LOG.log(Level.FINE,
			adPerson.getGivenName() + " | " + adPerson.getDistinguishedName() + " | "
				+ adPerson.getVaUserName() + " | " + adPerson.getTitle() + " | "
				+ adPerson.getDepartment() + " | " + adPerson.getEmail() + " | " + adPerson.getCity()
				+ " | " + adPerson.getPostalCode() + " | " + adPerson.getTelephoneNumber()); // debug
		stakeholderDao.populateVASIWithAdData(adPerson);
		if (adPerson.getDistinguishedName().equals("NOT_FOUND_IN_AD")) {
		    adPeronsNotFound.add(adPerson);
		} else {
		    addedPersons.add(adPerson);
		}
	    }
	}

	LOG.log(Level.INFO, "Total Records Updated in stakeholder class = " + addedPersons.size());
	vearStats.setVearUpdatedRecords(addedPersons.size());
	vearStats.setVearPeronsNotFound(adPeronsNotFound.size());
	vearStats.setNotFoundPersons(adPeronsNotFound);
	vearStats.setAddedPersons(addedPersons);

	adPersonsNotMapped = stakeholderDao.getStakeholdersWithOUTAdMapping();
	vearStats.setVearPersonsNotMapped(adPersonsNotMapped.size());
	vearStats.setNotMappedPersons(adPersonsNotMapped);

	estimatedTime = (System.nanoTime() - startTime) / 1000000;
	LOG.log(Level.FINE, "GAL Process Time in Seconds: " + TimeUnit.MILLISECONDS.toSeconds(estimatedTime));

	return vearStats;

    }

    public String personAsString(Person adPerson) {
	return "Person [stakeholderId=" + adPerson.getStakeholderId() + ", vaUserName=" + adPerson.getVaUserName()
		+ ", middleName=" + adPerson.getMiddleName() + ", firstName=" + adPerson.getFirstName() + ", lastName="
		+ adPerson.getLastName() + ", email=" + adPerson.getEmail() + ", title=" + adPerson.getTitle()
		+ ", telephoneNumber=" + adPerson.getTelephoneNumber() + ", distinguishedName="
		+ adPerson.getDistinguishedName() + "]";
    }

    private List<Task<Person, ADPersonRepository>> setupSearchBySamAccountNameTasks(List<Person> stakeholders) {
	List<Task<Person, ADPersonRepository>> tasks = new ArrayList<>();
	for (Person input : stakeholders) {
	    tasks.add(new SearchByVaUserNameTask(input));
	}
	return tasks;
    }

}
