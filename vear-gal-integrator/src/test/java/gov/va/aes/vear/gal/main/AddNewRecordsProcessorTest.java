package gov.va.aes.vear.gal.main;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import gov.va.aes.vear.gal.utils.FormatPhoneNumber;

public class AddNewRecordsProcessorTest {

    @Test
    public void testFormatPhone() {
	FormatPhoneNumber mrpp = new FormatPhoneNumber();

	assertEquals("(123) 345-8990", mrpp.formatPhone("123-345-8990", null));
	assertEquals("(123) 345-8990", mrpp.formatPhone(null, "123-345-8990"));
	assertEquals("(123) 345-8990", mrpp.formatPhone("123-345-8990,123-324-6789", null));
	assertEquals("(123) 345-8990 x1234", mrpp.formatPhone("123-345-8990 x1234", null));
    }
}
