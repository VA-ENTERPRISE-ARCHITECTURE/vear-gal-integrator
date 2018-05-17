package gov.va.aes.vear.gal.utils;

import java.util.concurrent.CountDownLatch;

import gov.va.aes.vear.gal.model.Person;
import gov.va.aes.vear.gal.repositories.ADPersonRepository;

public class SearchByEmailTask implements Task<Person, ADPersonRepository>{

	Person vaUser ;
	
	public SearchByEmailTask(Person input) {
		this.vaUser = input;
	}

	@Override
	public Person process(ADPersonRepository adRepo, CountDownLatch latch) {
		latch.countDown();
		System.out.println("Searching for email: "+ vaUser.getEmail());
		Person personByEmail = null;
		
				try {
					personByEmail = adRepo.getPersonByEmail(vaUser.getEmail());
					if(personByEmail == null ) {
						personByEmail = vaUser;
						personByEmail.setsAMAccountName("NOT_FOUND_IN_AD");
						personByEmail.setDomain("NOT_FOUND_IN_AD");
					}
					personByEmail.setElementId(vaUser.getElementId());
					personByEmail.setStakeholderId(vaUser.getStakeholderId());
						System.out.println(personByEmail.getGivenName()
								+ " | "	+ personByEmail.getDomain()
								+ " | " + personByEmail.getsAMAccountName()
								+ " | " + personByEmail.getTitle()
								+ " | " + personByEmail.getDepartment() 
								+ " | " + personByEmail.getEmail() 
								+ " | "	+ personByEmail.getTelephoneNumber() 
								+ " | " + personByEmail.getStreetAddress() 
								+ " | "	+ personByEmail.getCity() 
								+ " | " + personByEmail.getPostalCode());
				} catch (Exception eCT) {
					personByEmail = vaUser;
					personByEmail.setsAMAccountName("SEARCH_TIMEOUT_IN_AD");
					personByEmail.setDomain("SEARCH_TIMEOUT_IN_AD");
					personByEmail.setElementId(vaUser.getElementId());
					personByEmail.setStakeholderId(vaUser.getStakeholderId());
					System.out.println("AD Email search Exception :"+ eCT.getMessage());
					eCT.printStackTrace();
				}
		
		
		return personByEmail;
	}

}
