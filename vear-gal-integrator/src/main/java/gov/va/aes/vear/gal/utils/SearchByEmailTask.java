package gov.va.aes.vear.gal.utils;

import java.util.concurrent.CountDownLatch;

import gov.va.aes.vear.gal.model.Person;
import gov.va.aes.vear.gal.repositories.ADPersonRepository;

public class SearchByEmailTask implements Task<Person, ADPersonRepository>{

	String email ;
	
	public SearchByEmailTask(String email) {
		this.email = email;
	}

	@Override
	public Person process(ADPersonRepository adRepo, CountDownLatch latch) {
		latch.countDown();
		System.out.println("Searching for email: "+ email);
		Person personByEmail = adRepo.getPersonByEmail(email);
		if(personByEmail == null ) {
			Person person = new Person();
			person.setEmail(email);
			person.setGivenName("");
			person.setDomain("");
			person.setsAMAccountName("");
			person.setTitle("");
			person.setDepartment("") ;
			person.setTelephoneNumber("") ;
			person.setStreetAddress("") ;
			person.setCity("") ;
			person.setPostalCode("");
		}else {
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
		}
		return personByEmail;
	}

}
