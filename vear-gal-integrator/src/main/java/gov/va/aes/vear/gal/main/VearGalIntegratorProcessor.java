package gov.va.aes.vear.gal.main;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import gov.va.aes.vear.gal.model.VearStats;
import gov.va.aes.vear.gal.utils.EmailService;

@Component
public class VearGalIntegratorProcessor {
    private static final Logger LOG = Logger.getLogger(VearGalIntegratorProcessor.class.getName());

    @Autowired
    AddNewRecordsProcessor addNewRecordsProcessor;
    @Autowired
    CompareRecordsProcessor compareRecordProcessor;
    @Autowired
    public EmailService emailSender;
    @Autowired
    public Configuration freemarkerConfig;

    @Scheduled(cron = "0 30 7 ? * ?") // to schedule job every day at 7:30PM
    public void process() throws Exception {

	VearStats vearStatsCompare = null;
	VearStats vearStatsInitPull = null;
	try {
	    vearStatsInitPull = addNewRecordsProcessor.process();
	} catch (Exception e) {
	    // send Error Email To dev tools Team;
	    String errorText = getStringOutOfExceptionStackTrace(e);
	    // LOG.log(Level.SEVERE, "Email Text Start: \n");
	    // LOG.log(Level.SEVERE, errorText);
	    // LOG.log(Level.SEVERE, "Email Text Ends \n");
	    emailSender.sendAdminMessage(errorText);
	    throw e;
	}

	try {
	    vearStatsCompare = compareRecordProcessor.process();
	} catch (Exception e) {
	    // send Error Email To dev tools Team;
	    String errorText = getStringOutOfExceptionStackTrace(e);
	    // LOG.log(Level.SEVERE, "Email Text Start: \n");
	    // LOG.log(Level.SEVERE, errorText);
	    // LOG.log(Level.SEVERE, "Email Text Ends \n");
	    emailSender.sendAdminMessage(errorText);
	    throw e;

	}

	Map<String, Object> model = new HashMap<String, Object>();
	model.put("vearStatsInitPull", vearStatsInitPull);
	model.put("vearStatsCompare", vearStatsCompare);
	Template vearEmailTemplate = freemarkerConfig.getTemplate("vearStatsEmailTemplate.ftl");
	String text = FreeMarkerTemplateUtils.processTemplateIntoString(vearEmailTemplate, model);

	// LOG.log(Level.INFO, text);
	// mail notification
	emailSender.sendMessage(text);
    }

    protected String getStringOutOfExceptionStackTrace(final Exception e) {
	StringWriter errors = new StringWriter();
	e.printStackTrace(new PrintWriter(errors));
	return errors.toString();
    }
}
