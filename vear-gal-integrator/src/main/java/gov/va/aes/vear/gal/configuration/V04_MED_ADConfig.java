package gov.va.aes.vear.gal.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.ldap.repository.config.EnableLdapRepositories;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

@Configuration
@EnableLdapRepositories(basePackages = "gov.va.aes.vear.gal.repositories", ldapTemplateRef = "v04MedLdapTemplate")
public class V04_MED_ADConfig {

	@Autowired
	Environment env;

	@Bean
	public LdapContextSource v04MedContextSource() {
		LdapContextSource contextSource = new LdapContextSource();
		contextSource.setUrl(env.getRequiredProperty("ad.url"));
		contextSource.setBase("dc=v04,dc=med,dc=va,dc=gov");
		contextSource.setUserDn(env.getRequiredProperty("ad.service.user"));
		contextSource.setPassword(env.getRequiredProperty("ad.secret"));
		contextSource.setReferral("follow");
		return contextSource;
	}

	@Bean(name = "v04MedLdapTemplate")
	public LdapTemplate v04MedLdapTemplate() {
		return new LdapTemplate(v04MedContextSource());
	}
}