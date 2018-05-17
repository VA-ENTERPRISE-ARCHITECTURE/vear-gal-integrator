package gov.va.aes.vear.gal.utils;

import java.util.concurrent.CountDownLatch;

import gov.va.aes.vear.gal.model.Person;
import gov.va.aes.vear.gal.repositories.ADPersonRepository;

public class SearchByVaUserNameTask implements Task<Person, ADPersonRepository>{

	Person vaUser ;
	
	public SearchByVaUserNameTask(Person input) {
		this.vaUser = input;
	}

	@Override
	public Person process(ADPersonRepository adRepo, CountDownLatch latch) {
		latch.countDown();
		System.out.println("Searching for sAMAccountName: "+ vaUser.getsAMAccountName());
		Person personBySamAccountName = null;
		personBySamAccountName = adRepo.getPersonById(vaUser.getsAMAccountName());
		
		if(personBySamAccountName == null ) {
			personBySamAccountName = vaUser;
			personBySamAccountName.setsAMAccountName("NOT_FOUND_IN_AD");
		}
			personBySamAccountName.setElementId(vaUser.getElementId());
			personBySamAccountName.setStakeholderId(vaUser.getStakeholderId());
		
		return personBySamAccountName;
	}

}
