package gov.va.aes.vear.gal.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    Environment env;

    private static final Logger LOG = Logger.getLogger(EmailService.class.getName());

    public void sendMessage(final String mail) {

	String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

	JavaMailSenderImpl emailSender = new JavaMailSenderImpl();
	emailSender.setHost("smtp.va.gov");
	emailSender.setPort(25);
	Properties props = emailSender.getJavaMailProperties();
	props.put("mail.transport.protocol", "smtp");
	// props.put("mail.smtp.starttls.enable", "true");
	props.put("spring.mail.properties.mail.smtp.auth", "false");

	// mail notification
	MimeMessage message = emailSender.createMimeMessage();

	MimeMessageHelper helper;
	try {
	    helper = new MimeMessageHelper(message);
	    helper.setFrom(env.getRequiredProperty("vear.email.sender"));
	    helper.setTo(env.getRequiredProperty("vear.email.recepients"));
	    helper.setSubject("VEAR GAL Integration Run on  " + date);
	    helper.setText(mail, true);
	} catch (MessagingException e) {
	    e.printStackTrace();
	    LOG.log(Level.SEVERE, "Errored-Out sending email notification");
	}

    }

    public void sendAdminMessage(final String mail) {

	String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

	JavaMailSenderImpl emailSender = new JavaMailSenderImpl();
	emailSender.setHost("smtp.va.gov");
	emailSender.setPort(25);
	Properties props = emailSender.getJavaMailProperties();
	props.put("mail.transport.protocol", "smtp");
	// props.put("mail.smtp.starttls.enable", "true");
	props.put("spring.mail.properties.mail.smtp.auth", "false");

	// mail notification
	MimeMessage message = emailSender.createMimeMessage();

	MimeMessageHelper helper;
	try {
	    helper = new MimeMessageHelper(message);
	    helper.setFrom(env.getRequiredProperty("vear.email.sender"));
	    helper.setTo(env.getRequiredProperty("vear.email.Admin.recepients"));
	    helper.setSubject("VEAR GAL Integration Failed with Exception on  " + date);
	    helper.setText(mail, true);
	} catch (MessagingException e) {
	    e.printStackTrace();
	    LOG.log(Level.SEVERE, "Errored-Out sending Admin Email notification");
	}

    }

}