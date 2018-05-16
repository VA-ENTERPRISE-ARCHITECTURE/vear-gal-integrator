package gov.va.aes.vear.gal.processor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.va.aes.vear.gal.data.StakeholderDAO;
import gov.va.aes.vear.gal.model.Person;
import gov.va.aes.vear.gal.repositories.ADPersonRepository;
import gov.va.aes.vear.gal.utils.ADRecordsWriter;
import gov.va.aes.vear.gal.utils.AD_DumpWriter;
import gov.va.aes.vear.gal.utils.FileReader;
import gov.va.aes.vear.gal.utils.PersonResults;
import gov.va.aes.vear.gal.utils.SearchByEmailTask;
import gov.va.aes.vear.gal.utils.SearchByVaUserNameTask;
import gov.va.aes.vear.gal.utils.Task;
import gov.va.aes.vear.gal.utils.WaitingFuturesRunner;

@Component
public class MainAdRecordPullProcessor {
	@Autowired
	ADPersonRepository repository;
	@Autowired
	StakeholderDAO stakeholderDao;
	@Autowired
	ADRecordsWriter outputWriter;
	@Autowired
	FileReader fileReader;
	@Autowired
	AD_DumpWriter dumpWriter;
	
	public void process() {
		
	
		long startTime = System.nanoTime();
	
		List<Person> adPerons = new ArrayList<Person>();
	
		List<Person> stakeholders = fileReader.loadDBRecordsFromFile();
	
		System.out.println("------------------------------------ ");
		
		System.out.println("Total Records Read from Stakeholder table = " + stakeholders.size());
	
		final List<Task<Person, ADPersonRepository>> taskList = setupSearchByEmailTasks(stakeholders);
	
		final WaitingFuturesRunner<Person, ADPersonRepository> completableFuturesRunner = new WaitingFuturesRunner<>(
				taskList, 5, 4, TimeUnit.SECONDS);
		final PersonResults consolidatedResults = new PersonResults();
	
		completableFuturesRunner.go(repository, consolidatedResults);
	
		adPerons = consolidatedResults.getResults();
	
		System.out.println("AD pull size: " + adPerons.size());
	
		long estimatedTime = (System.nanoTime() - startTime) / 1000000;
	
		System.out.println("Response Time in Seconds: " + TimeUnit.MILLISECONDS.toSeconds(estimatedTime));
		
		System.out.println("Total Records Added to AD list = " + adPerons.size());
		
		// write searched AD records to file
		try {
			dumpWriter.writeOutput(adPerons);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private List<Task<Person, ADPersonRepository>> setupSearchByEmailTasks(List<Person> stakeholders) {
		// TODO Auto-generated method stub
		
		List<Task<Person, ADPersonRepository>> tasks = new ArrayList<>();
		for (Person input : stakeholders) {
			tasks.add(new SearchByEmailTask(input.getEmail()));
		}
		return tasks;
	}

	private List<Task<Person, ADPersonRepository>> setupSearchBySamAccountNameTasks(List<Person> stakeholders) {
		// TODO Auto-generated method stub
		
		List<Task<Person, ADPersonRepository>> tasks = new ArrayList<>();
		for (Person input : stakeholders) {
			tasks.add(new SearchByVaUserNameTask(input.getsAMAccountName()));
		}
		return tasks;
	}

}
