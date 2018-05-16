package gov.va.aes.vear.gal.repositories;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import java.util.LinkedList;
import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.control.PagedResultsDirContextProcessor;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapOperationsCallback;
import org.springframework.ldap.core.support.SingleContextSource;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.SearchScope;
import org.springframework.stereotype.Service;

import gov.va.aes.vear.gal.model.Person;

@Service
public class ADPersonRepository {
	private static final String[] RETURN_ATTRIBUTES = { "userPrincipalName", "sAMAccountName", "givenName",
			"middleName", "cn", "sn", "mail", "title", "telephoneNumber", "mobile", "streetAddress", "l", "st",
			"postalCode", "department", "distinguishedName" };

	@Autowired
	LdapTemplate dvaLdapTemplate;

	@Autowired
	LdapTemplate vhaLdapTemplate;

	@Autowired
	LdapTemplate vaLdapTemplate;
	
	@Autowired
	LdapTemplate medLdapTemplate;
	
	@Autowired
	LdapTemplate vbaLdapTemplate; 
	
	@Autowired
	LdapTemplate cemLdapTemplate;
	
	public List<Person> getAllPersons(String domain) {

		LdapQuery query = query().searchScope(SearchScope.SUBTREE).attributes(RETURN_ATTRIBUTES).where("sAMAccountType")
				.is("805306368");
		
		return dvaLdapTemplate.search(query, new ADPersonAttributesMapper());
		
	}
	
	public  List<Person> getAllPersonsPaged() {
		
		final SearchControls searchControls = new SearchControls();  
	     searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);  
	     //searchControls.setDerefLinkFlag(true);
	     final PagedResultsDirContextProcessor processor = new PagedResultsDirContextProcessor(2000);  
	     return SingleContextSource.doWithSingleContext(  
	    		 medLdapTemplate.getContextSource(), new LdapOperationsCallback<List<Person>>() {  
	       @Override  
	       public List<Person> doWithLdapOperations(LdapOperations operations) {  
	         List<Person> result = new LinkedList<Person>();  
	         boolean isFinished = false;  
	         while (!isFinished) {  
	           List<Person> oneResult = operations.search(  
	               "",  
	               "(sAMAccountType=805306368)",  
	               searchControls,  
	               new ADPersonAttributesMapper(),processor  
	           );  
	           result.addAll(oneResult);  
	           if (processor.hasMore()) {  
	             System.out.println ("processor has more: Paging Result Size: " + oneResult.size() + ", Total Size is " + result.size());  
	           } else {  
	             isFinished = true;  
	           }  
	         };  
	         return result;  
	       }  
	     }  
	     );
	}
	
public  List<Person> getAllPersonsPagedPerDomian(LdapTemplate template) {
		
		final SearchControls searchControls = new SearchControls();  
	     searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);  
	     //searchControls.setDerefLinkFlag(true);
	     final PagedResultsDirContextProcessor processor = new PagedResultsDirContextProcessor(2500);  
	     return SingleContextSource.doWithSingleContext(  
	    		 template.getContextSource(), new LdapOperationsCallback<List<Person>>() {  
	       @Override  
	       public List<Person> doWithLdapOperations(LdapOperations operations) {  
	         List<Person> result = new LinkedList<Person>();  
	         boolean isFinished = false;  
	         while (!isFinished) {  
	           List<Person> oneResult = operations.search(  
	               "",  
	               "(sAMAccountType=805306368)",//"(&(objectCategory=Person)(sAMAccountType=805306368))",  
	               searchControls,  
	               new ADPersonAttributesMapper(),processor  
	           );  
	           result.addAll(oneResult);  
	           if (processor.hasMore()) {  
	             System.out.println ("processor has more: Paging Result Size: " + oneResult.size() + ", Total Size is " + result.size());  
	           } else {  
	             isFinished = true;  
	           }  
	         };  
	         return result;  
	       }  
	     }  
	     );
	}

	public List<Person> getTotalPersonsPagedForAllDomains(){
		List<Person> result = new LinkedList<Person>();  
		System.out.println("-----------------DVA------------ " );
		List<Person> dvaPersons = getAllPersonsPagedPerDomian(dvaLdapTemplate);
		result.addAll(dvaPersons);  
		System.out.println("-----------------VHA------------ " );
		List<Person> vhaPersons = getAllPersonsPagedPerDomian(vhaLdapTemplate);
		result.addAll(vhaPersons);  
		System.out.println("-----------------CEM------------ " );
		List<Person> cemPersons = getAllPersonsPagedPerDomian(cemLdapTemplate);
		result.addAll(cemPersons);
		System.out.println("-----------------VBA------------ " );
		List<Person> vbaPersons = getAllPersonsPagedPerDomian(vbaLdapTemplate);
		result.addAll(vbaPersons);
		System.out.println("-----------------MED------------ " );
		List<Person> medPersons = getAllPersonsPagedPerDomian(medLdapTemplate);
		result.addAll(medPersons);  
		return result;
		
	}

	public Person getPersonById(String sAMAccountName) {

		List<Person> results = null;;

		LdapQuery query = query().searchScope(SearchScope.SUBTREE).attributes(RETURN_ATTRIBUTES).where("sAMAccountType")
				.is("805306368").and("sAMAccountName").is(sAMAccountName);

		if (sAMAccountName.toLowerCase().startsWith("vaco"))
			results = dvaLdapTemplate.search(query, new ADPersonAttributesMapper());
		else if (sAMAccountName.toLowerCase().startsWith("vha"))
			results = vhaLdapTemplate.search(query, new ADPersonAttributesMapper());
			if (results == null || results.size() == 0) {	
				results = medLdapTemplate.search(query, new ADPersonAttributesMapper());
			}
			
		else
			results = vaLdapTemplate.search(query, new ADPersonAttributesMapper());

		if (results.size() == 0) {			
			System.out.println(" No matches found for " + sAMAccountName);
			return null;
		} else if (results.size() == 1) {
			return results.get(0);
		} else {
			System.out.println(" Multiple matches found for " + sAMAccountName);
			return results.get(0);
		}

	}
	
	public Person getPersonByEmail(String email) {

		List<Person> results = null;;

		LdapQuery query = query().searchScope(SearchScope.SUBTREE).attributes(RETURN_ATTRIBUTES).where("sAMAccountType")
				.is("805306368").and("mail").is(email);

		results = dvaLdapTemplate.search(query, new ADPersonAttributesMapper());
		if (results.size() == 0) {
			// Do Nothing
		} else if (results.size() == 1) {
			return results.get(0);
		} else {
			System.out.println(" Multiple matches found for " + email);
			return results.get(0);
		}
		results = vhaLdapTemplate.search(query, new ADPersonAttributesMapper());
		if (results.size() == 0) {
			// Do Nothing
		} else if (results.size() == 1) {
			return results.get(0);
		} else {
			System.out.println(" Multiple matches found for " + email);
			return results.get(0);
		}
		results = vbaLdapTemplate.search(query, new ADPersonAttributesMapper());
		if (results.size() == 0) {
			// Do Nothing
		} else if (results.size() == 1) {
			return results.get(0);
		} else {
			System.out.println(" Multiple matches found for " + email);
			return results.get(0);
		}
		results = cemLdapTemplate.search(query, new ADPersonAttributesMapper());
		if (results.size() == 0) {
			// Do Nothing
		} else if (results.size() == 1) {
			return results.get(0);
		} else {
			System.out.println(" Multiple matches found for " + email);
			return results.get(0);
		}
		results = medLdapTemplate.search(query, new ADPersonAttributesMapper());
		if (results.size() == 0) {
			// Do Nothing
		} else if (results.size() == 1) {
			return results.get(0);
		} else {
			System.out.println(" Multiple matches found for " + email);
			return results.get(0);
		}
		results = vaLdapTemplate.search(query, new ADPersonAttributesMapper());

		if (results.size() == 0) {
			System.out.println(" No matches found for " + email);
			return null;
		} else if (results.size() == 1) {
			return results.get(0);
		} else {
			System.out.println(" Multiple matches found for " + email);
			return results.get(0);
		}

	}

	/**
	 * Custom ADPerson attributes mapper, maps the attributes to the ADPerson POJO
	 */
	private class ADPersonAttributesMapper implements AttributesMapper<Person> {
		public Person mapFromAttributes(Attributes attrs) throws NamingException {
			Person person = new Person();
			person.setUserPrincipalName(findAttrVal(attrs, "userPrincipalName"));
			person.setsAMAccountName(findAttrVal(attrs, "sAMAccountName"));
			person.setFirstName(findAttrVal(attrs, "givenName"));
			person.setGivenName(findAttrVal(attrs, "cn"));
			person.setLastName(findAttrVal(attrs, "sn"));
			person.setMiddleName(findAttrVal(attrs, "middleName"));
			person.setTitle(findAttrVal(attrs, "title"));
			person.setEmail(findAttrVal(attrs, "mail"));
			person.setTelephoneNumber(findAttrVal(attrs, "telephoneNumber"));
			person.setMobile(findAttrVal(attrs, "mobile"));
			person.setStreetAddress(findAttrVal(attrs, "streetAddress"));
			person.setCity(findAttrVal(attrs, "l"));
			person.setState(findAttrVal(attrs, "st"));
			person.setPostalCode(findAttrVal(attrs, "postalCode"));
			person.setDepartment(findAttrVal(attrs, "department"));
			person.setDomain(findAttrVal(attrs, "distinguishedName"));

			return person;
		}

		private String findAttrVal(Attributes attrs, String attrName) throws NamingException {
			if (attrs.get(attrName) != null) {
				return (String) attrs.get(attrName).get();
			}
			return null;
		}
	}

}
