package gov.va.aes.vear.gal.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import gov.va.aes.vear.gal.model.Person;

@Repository
public class StakeholderDAO {

    private static final Logger LOG = Logger.getLogger(StakeholderDAO.class.getName());

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Person> getStakeholdersWithDomain() {
	return jdbcTemplate.query(STAKEHOLDER_WITH_DOMAIN_QUERY, new StakeholderMapper());
    }

    public List<Person> getStakeholdersWithOUTDomain() {
	return jdbcTemplate.query(STAKEHOLDER_WITHOUT_DOMAIN_QUERY, new StakeholderMapper());
    }

    public List<String> getADDomains() {
	return jdbcTemplate.queryForList(DOMAIN_QUERY, String.class);
    }

    public List<Person> getStakeholdersWithOUTAdMapping() {
	return jdbcTemplate.query(STAKEHOLDER_WITHOUT_AD_MAPPING, new StakeholderMapper());
    }

    public List<Person> getStakeholdersNotFoundInAdDomain() {
	return jdbcTemplate.query(STAKEHOLDER_WITH_NOT_FOUND_IN_AD, new StakeholderMapper());
    }

    public boolean isVASIDBAccessible() {
	int i = 0;
	try {
	    i = jdbcTemplate.queryForObject("SELECT 1 FROM DUAL", Integer.class);
	} catch (Exception e) {
	    LOG.log(Level.SEVERE, "Not able to establish connection to VASI Database.");
	    return false;
	}

	if (i == 1) {
	    LOG.log(Level.INFO, "Successfully connected to VASI Database.");
	    return true;
	}
	return false;
    }

    public int populateVASIWithAdData(Person adUser) {
	if (adUser.getFirstName() != null && adUser.getLastName() != null) {

	    int rows = jdbcTemplate.update(STAKEHOLDER_UPDATE_QUERY, adUser.getDistinguishedName(),
		    adUser.getFirstName(), adUser.getLastName(), adUser.getMiddleName(), adUser.getTitle(),
		    adUser.getTelephoneNumber(), adUser.getEmail(), adUser.getStakeholderId());

	    return rows;
	}
	return 0;

    }

    private static final class StakeholderMapper implements RowMapper<Person> {
	public Person mapRow(ResultSet rs, int rowNumber) throws SQLException {
	    Person dbPerson = new Person();
	    dbPerson.setElementId(rs.getBigDecimal("element_id"));
	    dbPerson.setStakeholderId(rs.getBigDecimal("stakeholder_id"));
	    dbPerson.setVaUserName(rs.getString("va_user_name"));
	    dbPerson.setFirstName(rs.getString("first_name"));
	    dbPerson.setLastName(rs.getString("last_name"));
	    dbPerson.setMiddleName(rs.getString("middle_initial"));
	    dbPerson.setSuffix(rs.getString("suffix"));
	    dbPerson.setTitle(rs.getString("title"));
	    dbPerson.setTelephoneNumber(rs.getString("phone"));
	    dbPerson.setEmail(rs.getString("email"));
	    dbPerson.setDistinguishedName(rs.getString("ad_domain"));
	    dbPerson.setConnectionDomain(rs.getString("connection_domain"));
	    return dbPerson;
	}
    }

    private static final String DOMAIN_QUERY = "select DISTINCT(SUBSTR(ad_domain, REGEXP_INSTR(ad_domain, '(DC=[a-zA-Z0-9]*)'))) as connection_domain"
	    + " from ee.element_attr_c402 stakeholder"
	    + " where ad_domain is not null and ad_domain NOT LIKE 'NOT_FOUND_IN_AD%' and email_type = 1565"
	    + " order by connection_domain";

    private static String STAKEHOLDER_WITH_DOMAIN_QUERY = "select element_id,stakeholder_id, va_user_name,first_name, "
	    + "middle_initial,last_name, suffix, title, email, phone, "
	    + "SUBSTR(ad_domain, REGEXP_INSTR(ad_domain, '(DC=[a-zA-Z0-9]*)')) as connection_domain, ad_domain"
	    + " from ee.element_attr_c402 stakeholder "
	    + " where ad_domain is not null and ad_domain NOT LIKE 'NOT_FOUND_IN_AD%'"
	    + " and va_user_name not in ('LindaFerroNARA', 'vacorothk', 'RobYoos', 'vaaacmaratm', 'vaaacwilliw', 'vacoWinteJ', 'VBAROAReynoR', 'VHAHINHiggiC', 'vaaitcpechlb', 'VHAISDWARKP', 'vhahacsitlet')"
	    + " and email_type = 1565" + " order by connection_domain, last_name, first_name";

    private static String STAKEHOLDER_WITHOUT_DOMAIN_QUERY = "select element_id, stakeholder_id, va_user_name,first_name, "
	    + "middle_initial,last_name, suffix, title, email, phone, '' as connection_domain, ad_domain"
	    + " from ee.element_attr_c402 stakeholder " + "where ad_domain is null and va_user_name is not null"
	    + " and email_type = 1565";

    private static String STAKEHOLDER_WITHOUT_AD_MAPPING = "select element_id, stakeholder_id, va_user_name,first_name, middle_initial,last_name, suffix, title, email, phone, '' as connection_domain, ad_domain"
	    + " from ee.element_attr_c402 stakeholder " + "where va_user_name is null";

    private static final String STAKEHOLDER_UPDATE_QUERY = "UPDATE ee.element_attr_c402 SET ad_domain = ?,FIRST_NAME = ?, LAST_NAME = ?, middle_initial = ?, title = ?, phone = ?, email = ?, email_type = 1565 WHERE stakeholder_id = ?";

    private static String STAKEHOLDER_WITH_NOT_FOUND_IN_AD = "select element_id, stakeholder_id, va_user_name,first_name, middle_initial,last_name, suffix, title, email, phone, '' as connection_domain, ad_domain"
	    + " from ee.element_attr_c402 stakeholder where ad_domain LIKE 'NOT_FOUND_IN_AD%'"
	    + " and va_user_name not in ('LindaFerroNARA', 'vacorothk', 'RobYoos', 'vaaacmaratm', 'vaaacwilliw', 'vacoWinteJ', 'VBAROAReynoR', 'VHAHINHiggiC', 'vaaitcpechlb', 'VHAISDWARKP', 'vhahacsitlet')"
	    + " and email_type = 1565";
}
