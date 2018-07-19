package gov.va.aes.vear.gal.utils;

import org.springframework.stereotype.Component;

@Component
public class FormatPhoneNumber {

    public String formatPhone(String phoneNum, String mobileNum) {

	if (phoneNum != null) {
	    String p1 = phoneNum.replaceAll("[^\\d]", "");
	    if (p1.length() == 10 || p1.length() == 20)
		return p1.replaceFirst("(\\d{3})(\\d{3})(\\d{4})(\\d*)", "($1) $2-$3");
	    else if (p1.length() > 10 && p1.length() < 20)
		return p1.replaceFirst("(\\d{3})(\\d{3})(\\d{4})(\\d+)", "($1) $2-$3 x$4");
	    else
		return p1.replaceFirst("(\\d{3})(\\d{3})(\\d{4})(\\d+)", "($1) $2-$3 x$4");
	} else if (mobileNum != null) {
	    String p2 = mobileNum.replaceAll("[^\\d]", "");
	    return p2.replaceFirst("(\\d{3})(\\d{3})(\\d{4})", "($1) $2-$3");
	} else {
	    return null;
	}
    }

}
