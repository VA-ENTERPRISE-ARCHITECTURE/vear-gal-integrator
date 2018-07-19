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
import gov.va.aes.vear.gal.utils.CompareVaUserTask;
import gov.va.aes.vear.gal.utils.FormatPhoneNumber;
import gov.va.aes.vear.gal.utils.PersonResults;
import gov.va.aes.vear.gal.utils.Task;
import gov.va.aes.vear.gal.utils.WaitingFuturesRunner;

@Component
public class CompareRecordsProcessor {

    private static final Logger LOG = Logger.getLogger(CompareRecordsProcessor.class.getName());

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
	if (vearStats.getRunDate() == null) {
	    vearStats.setRunDate(date);
	}
	long startTime = System.nanoTime();
	long estimatedTime = 0;

	List<Person> adPerons = new ArrayList<Person>();
	List<Person> updatedPersons = new ArrayList<Person>();
	List<Person> adPeronsNotFound = new ArrayList<Person>();
	List<Person> stakeholders = stakeholderDao.getStakeholdersWithDomain();
	// Adding current disabled records from Stakeholder class
	List<Person> stakeholdersNotFound = stakeholderDao.getStakeholdersNotFoundInAdDomain();
	for (Person stakeholderNotFound : stakeholdersNotFound) {
	    adPeronsNotFound.add(stakeholderNotFound);
	}
	LOG.log(Level.INFO, "Begin Process ");
	LOG.log(Level.INFO, "Total Records Read from Stakeholder table = " + stakeholders.size());
	vearStats.setVearRecordsRead(stakeholders.size());

	estimatedTime = (System.nanoTime() - startTime) / 1000000;
	LOG.log(Level.FINE, "VEAR DB access Time in Seconds: " + TimeUnit.MILLISECONDS.toSeconds(estimatedTime));

	startTime = System.nanoTime();

	final List<Task<Person, ADPersonRepository>> taskList = setupCompareVaUserTasks(stakeholders);

	final WaitingFuturesRunner<Person, ADPersonRepository> completableFuturesRunner = new WaitingFuturesRunner<>(
		taskList, 25, 4, TimeUnit.SECONDS);
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
				+ " | " + adPerson.getPostalCode() + " | " + adPerson.getTelephoneNumber());
		stakeholderDao.populateVASIWithAdData(adPerson);

		if (adPerson.getDistinguishedName().equals("NOT_FOUND_IN_AD")) {
		    adPeronsNotFound.add(adPerson);
		} else {
		    updatedPersons.add(adPerson);
		}
	    }
	}

	LOG.log(Level.INFO, "Total Records Updated in stakeholder class = " + updatedPersons.size());
	vearStats.setVearUpdatedRecords(updatedPersons.size());
	vearStats.setVearDisableRecords(adPeronsNotFound.size());
	vearStats.setDisabledPersons(adPeronsNotFound);
	vearStats.setUpdatedPersons(updatedPersons);

	estimatedTime = (System.nanoTime() - startTime) / 1000000;
	LOG.log(Level.INFO, "GAL Process Time in Seconds: " + TimeUnit.MILLISECONDS.toSeconds(estimatedTime));

	return vearStats;

    }

    private List<Task<Person, ADPersonRepository>> setupCompareVaUserTasks(List<Person> stakeholders) {
	List<Task<Person, ADPersonRepository>> tasks = new ArrayList<>();
	for (Person input : stakeholders) {
	    tasks.add(new CompareVaUserTask(input));
	}
	return tasks;
    }

}
