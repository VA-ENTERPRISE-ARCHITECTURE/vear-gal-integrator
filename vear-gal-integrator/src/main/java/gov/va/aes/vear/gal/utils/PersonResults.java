package gov.va.aes.vear.gal.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gov.va.aes.vear.gal.model.Person;

public class PersonResults implements ConsolidatedResult<Person> {

	private final List<Person> results = new ArrayList<>();

	@Override
	public void addResult(final Person result) {
		if (result != null)
			results.add(result);
	}

	public List<Person> getResults() {
		return Collections.unmodifiableList(results);
	}
}
