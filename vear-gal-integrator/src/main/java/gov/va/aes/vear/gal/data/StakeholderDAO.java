package gov.va.aes.vear.gal.data;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import gov.va.aes.vear.gal.model.Person;

@Repository
public class StakeholderDAO {
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<Person> loadVASIData() {
		return jdbcTemplate.query(STAKEHOLDER_QUERY, new StakeholderMapper());
	}
	
	public int populateVASIData(String vaUserName, String adDomain, BigDecimal id) {

		// define query arguments
		Object[] params = { vaUserName, adDomain, id };

		// define SQL types of the arguments
		int[] types = { Types.VARCHAR, Types.VARCHAR, Types.BIGINT };

		int rows = jdbcTemplate.update(STAKEHOLDER_UPDATE_QUERY, params, types);

		return rows;

	}

	public boolean isVASIDBAccessible() {
		int i = 0;
		try {
			i = jdbcTemplate.queryForObject("SELECT 1 FROM DUAL", Integer.class);
		} catch (Exception e) {
			System.out.println("Not able to establish connection to VASI Database.");
			return false;
		}

		if (i == 1){
			System.out.println("Successfully connected to VASI Database.");
			return true;
		}
		return false;
	}

	private static final class StakeholderMapper implements RowMapper<Person> {
		public Person mapRow(ResultSet rs, int rowNumber) throws SQLException {
			Person dbPerson = new Person();
			dbPerson.setElementId(rs.getBigDecimal("element_id"));
			dbPerson.setStakeholderId(rs.getBigDecimal("stakeholder_id"));
			dbPerson.setsAMAccountName(rs.getString("va_user_name"));
			dbPerson.setFirstName(rs.getString("first_name"));
			dbPerson.setLastName(rs.getString("last_name"));
			dbPerson.setMiddleName(rs.getString("middle_initial"));
			dbPerson.setTitle(rs.getString("title"));
			dbPerson.setTelephoneNumber(rs.getString("phone"));
			dbPerson.setEmail(rs.getString("email"));
			dbPerson.setDomain(rs.getString("ad_domain"));
			return dbPerson;
		}
	}

	private static String STAKEHOLDER_QUERY = "select element_id, stakeholder_id, va_user_name, ad_domain, first_name, last_name, middle_initial, title, phone, email from ee.element_attr_c402 where email is not null and email like '%@%' order by stakeholder_id";
	
	private static final String STAKEHOLDER_UPDATE_QUERY = "UPDATE ee.element_attr_c402 SET va_user_name = ? and ad_domain = ? WHERE stakeholder_id = ?";

}
