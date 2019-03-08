package gov.va.aes.vear.gal.repositories;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.SearchScope;
import org.springframework.stereotype.Service;

import gov.va.aes.vear.gal.model.Person;

@Service
public class ADPersonRepository implements InitializingBean {

    private static final Logger LOG = Logger.getLogger(ADPersonRepository.class.getName());

    private static final String[] RETURN_ATTRIBUTES = { "userPrincipalName", "sAMAccountName", "givenName",
	    "middleName", "cn", "sn", "mail", "title", "telephoneNumber", "mobile", "streetAddress", "l", "st",
	    "postalCode", "department", "distinguishedName" };

    @Autowired
    LdapTemplate aacLdapTemplate;

    @Autowired
    LdapTemplate dvaLdapTemplate;

    @Autowired
    LdapTemplate vhaMedLdapTemplate;

    @Autowired
    LdapTemplate vaLdapTemplate;

    @Autowired
    LdapTemplate medLdapTemplate;

    @Autowired
    LdapTemplate vbaLdapTemplate;

    @Autowired
    LdapTemplate cemLdapTemplate;

    @Autowired
    LdapTemplate v01MedLdapTemplate;

    @Autowired
    LdapTemplate v02MedLdapTemplate;

    @Autowired
    LdapTemplate v03MedLdapTemplate;

    @Autowired
    LdapTemplate v04MedLdapTemplate;

    @Autowired
    LdapTemplate v05MedLdapTemplate;

    @Autowired
    LdapTemplate v06MedLdapTemplate;

    @Autowired
    LdapTemplate v07MedLdapTemplate;

    @Autowired
    LdapTemplate v08MedLdapTemplate;

    @Autowired
    LdapTemplate v09MedLdapTemplate;

    @Autowired
    LdapTemplate v10MedLdapTemplate;

    @Autowired
    LdapTemplate v11MedLdapTemplate;

    @Autowired
    LdapTemplate v12MedLdapTemplate;

    @Autowired
    LdapTemplate v15MedLdapTemplate;

    @Autowired
    LdapTemplate v16MedLdapTemplate;

    @Autowired
    LdapTemplate v17MedLdapTemplate;

    @Autowired
    LdapTemplate v18MedLdapTemplate;

    @Autowired
    LdapTemplate v19MedLdapTemplate;

    @Autowired
    LdapTemplate v20MedLdapTemplate;

    @Autowired
    LdapTemplate v21MedLdapTemplate;

    @Autowired
    LdapTemplate v22MedLdapTemplate;

    @Autowired
    LdapTemplate v23MedLdapTemplate;

    Map<String, LdapTemplate> ldapTemplatesMap;

    @Override
    public void afterPropertiesSet() throws Exception {

	ldapTemplatesMap = new HashMap<>();
	ldapTemplatesMap.put("dc=dva,dc=va,dc=gov", dvaLdapTemplate);
	ldapTemplatesMap.put("dc=aac,dc=dva,dc=va,dc=gov", aacLdapTemplate);
	ldapTemplatesMap.put("dc=med,dc=va,dc=gov", medLdapTemplate);
	ldapTemplatesMap.put("dc=cem,dc=va,dc=gov", cemLdapTemplate);
	ldapTemplatesMap.put("dc=vba,dc=va,dc=gov", vbaLdapTemplate);
	ldapTemplatesMap.put("dc=vha,dc=med,dc=va,dc=gov", vhaMedLdapTemplate);
	ldapTemplatesMap.put("dc=v01,dc=med,dc=va,dc=gov", v01MedLdapTemplate);
	ldapTemplatesMap.put("dc=v02,dc=med,dc=va,dc=gov", v02MedLdapTemplate);
	ldapTemplatesMap.put("dc=v03,dc=med,dc=va,dc=gov", v03MedLdapTemplate);
	ldapTemplatesMap.put("dc=v04,dc=med,dc=va,dc=gov", v04MedLdapTemplate);
	ldapTemplatesMap.put("dc=v05,dc=med,dc=va,dc=gov", v05MedLdapTemplate);
	ldapTemplatesMap.put("dc=v06,dc=med,dc=va,dc=gov", v06MedLdapTemplate);
	ldapTemplatesMap.put("dc=v07,dc=med,dc=va,dc=gov", v07MedLdapTemplate);
	ldapTemplatesMap.put("dc=v08,dc=med,dc=va,dc=gov", v08MedLdapTemplate);
	ldapTemplatesMap.put("dc=v09,dc=med,dc=va,dc=gov", v09MedLdapTemplate);
	ldapTemplatesMap.put("dc=v10,dc=med,dc=va,dc=gov", v10MedLdapTemplate);
	ldapTemplatesMap.put("dc=v11,dc=med,dc=va,dc=gov", v11MedLdapTemplate);
	ldapTemplatesMap.put("dc=v12,dc=med,dc=va,dc=gov", v12MedLdapTemplate);
	ldapTemplatesMap.put("dc=v15,dc=med,dc=va,dc=gov", v15MedLdapTemplate);
	ldapTemplatesMap.put("dc=v16,dc=med,dc=va,dc=gov", v16MedLdapTemplate);
	ldapTemplatesMap.put("dc=v17,dc=med,dc=va,dc=gov", v17MedLdapTemplate);
	ldapTemplatesMap.put("dc=v18,dc=med,dc=va,dc=gov", v18MedLdapTemplate);
	ldapTemplatesMap.put("dc=v19,dc=med,dc=va,dc=gov", v19MedLdapTemplate);
	ldapTemplatesMap.put("dc=v20,dc=med,dc=va,dc=gov", v20MedLdapTemplate);
	ldapTemplatesMap.put("dc=v21,dc=med,dc=va,dc=gov", v21MedLdapTemplate);
	ldapTemplatesMap.put("dc=v22,dc=med,dc=va,dc=gov", v22MedLdapTemplate);
	ldapTemplatesMap.put("dc=v23,dc=med,dc=va,dc=gov", v23MedLdapTemplate);
	// special mapping for mpi.v21.med.va.gov
	ldapTemplatesMap.put("dc=mpi,dc=v21,dc=med,dc=va,dc=gov", v21MedLdapTemplate);
	// special mapping for no domain
	ldapTemplatesMap.put("default", vaLdapTemplate);
    }

    public Person getPersonByEmail(String email) {

	List<Person> results = null;

	LdapQuery query = query().searchScope(SearchScope.SUBTREE).attributes(RETURN_ATTRIBUTES).where("sAMAccountType")
		.is("805306368").and("mail").is(email);

	results = dvaLdapTemplate.search(query, new ADPersonAttributesMapper());
	if (results.size() == 0) {
	    // Do Nothing
	} else if (results.size() == 1) {
	    return results.get(0);
	} else {
	    LOG.log(Level.INFO, " Multiple matches found for " + email);
	    return results.get(0);
	}
	results = vhaMedLdapTemplate.search(query, new ADPersonAttributesMapper());
	if (results.size() == 0) {
	    // Do Nothing
	} else if (results.size() == 1) {
	    return results.get(0);
	} else {
	    LOG.log(Level.INFO, " Multiple matches found for " + email);
	    return results.get(0);
	}
	results = vbaLdapTemplate.search(query, new ADPersonAttributesMapper());
	if (results.size() == 0) {
	    // Do Nothing
	} else if (results.size() == 1) {
	    return results.get(0);
	} else {
	    LOG.log(Level.INFO, " Multiple matches found for " + email);
	    return results.get(0);
	}
	results = cemLdapTemplate.search(query, new ADPersonAttributesMapper());
	if (results.size() == 0) {
	    // Do Nothing
	} else if (results.size() == 1) {
	    return results.get(0);
	} else {
	    LOG.log(Level.INFO, " Multiple matches found for " + email);
	    return results.get(0);
	}
	results = medLdapTemplate.search(query, new ADPersonAttributesMapper());
	if (results.size() == 0) {
	    // Do Nothing
	} else if (results.size() == 1) {
	    return results.get(0);
	} else {
	    LOG.log(Level.INFO, " Multiple matches found for " + email);
	    return results.get(0);
	}
	results = vaLdapTemplate.search(query, new ADPersonAttributesMapper());

	if (results.size() == 0) {
	    LOG.log(Level.INFO, " No matches found for " + email);
	    return null;
	} else if (results.size() == 1) {
	    return results.get(0);
	} else {
	    LOG.log(Level.INFO, " Multiple matches found for " + email);
	    return results.get(0);
	}

    }

    public Person getPersonById(String sAMAccountName) {

	List<Person> results = null;

	LdapQuery query = query().searchScope(SearchScope.SUBTREE).attributes(RETURN_ATTRIBUTES).where("sAMAccountType")
		.is("805306368").and("sAMAccountName").is(sAMAccountName);

	results = vaLdapTemplate.search(query, new ADPersonAttributesMapper());

	if (results.size() == 0) {
	    LOG.log(Level.INFO, " No matches found for " + sAMAccountName);
	    return null;
	} else if (results.size() == 1) {
	    return results.get(0);
	} else {
	    LOG.log(Level.INFO, " Multiple matches found for " + sAMAccountName);
	    return results.get(0);
	}

    }

    public Person getPersonByIdAndDomain(String sAMAccountName, String domain) throws Exception {

	String searchBase = domain.toLowerCase();

	List<Person> results = null;
	LOG.log(Level.INFO, "AD Search for " + sAMAccountName + " and searchBase :" + searchBase);

	LdapQuery query = query().searchScope(SearchScope.SUBTREE).attributes(RETURN_ATTRIBUTES).where("sAMAccountType")
		.is("805306368").and("sAMAccountName").is(sAMAccountName);

	results = ldapTemplatesMap.get(searchBase).search(query, new ADPersonAttributesMapper());

	// try {
	// results = ldapTemplatesMap.get(searchBase).search(query, new
	// ADPersonAttributesMapper());
	// } catch (Exception e) {
	// LOG.log(Level.SEVERE,
	// "AD Search failed with exception " + sAMAccountName + " and searchBase :" +
	// searchBase);
	// return null;
	// }

	if (results.size() == 0) {
	    return null;
	} else if (results.size() == 1) {
	    return results.get(0);
	} else {
	    LOG.log(Level.INFO, " Multiple matches found for " + sAMAccountName + " and searchBase :" + searchBase);
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
	    person.setVaUserName(findAttrVal(attrs, "sAMAccountName"));
	    person.setFirstName(findAttrVal(attrs, "givenName"));
	    person.setGivenName(findAttrVal(attrs, "cn"));
	    person.setLastName(findAttrVal(attrs, "sn"));
	    person.setMiddleName(findAttrVal(attrs, "middleName"));
	    person.setTitle(findAttrVal(attrs, "title"));
	    person.setEmail(findAttrVal(attrs, "mail"));
	    person.setTelephoneNumber(findAttrVal(attrs, "telephoneNumber"));
	    // person.setMobile(findAttrVal(attrs, "mobile"));
	    person.setStreetAddress(findAttrVal(attrs, "streetAddress"));
	    person.setCity(findAttrVal(attrs, "l"));
	    person.setState(findAttrVal(attrs, "st"));
	    person.setPostalCode(findAttrVal(attrs, "postalCode"));
	    person.setDepartment(findAttrVal(attrs, "department"));
	    person.setDistinguishedName(findAttrVal(attrs, "distinguishedName"));

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
