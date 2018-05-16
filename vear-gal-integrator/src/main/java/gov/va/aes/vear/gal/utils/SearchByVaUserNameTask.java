package gov.va.aes.vear.gal.utils;

import java.util.concurrent.CountDownLatch;

import gov.va.aes.vear.gal.model.Person;
import gov.va.aes.vear.gal.repositories.ADPersonRepository;

public class SearchByVaUserNameTask implements Task<Person, ADPersonRepository>{

	String sAMAccountName ;
	
	public SearchByVaUserNameTask(String sAMAccountName) {
		this.sAMAccountName = sAMAccountName;
	}

	@Override
	public Person process(ADPersonRepository adRepo, CountDownLatch latch) {
		latch.countDown();
		System.out.println("Searching for sAMAccountName: "+ sAMAccountName);
		Person personBySamAccountName = adRepo.getPersonById(sAMAccountName);
		if(personBySamAccountName == null ) {
			Person person = new Person();
			person.setsAMAccountName(sAMAccountName);
		}
		return personBySamAccountName;
	}

}
