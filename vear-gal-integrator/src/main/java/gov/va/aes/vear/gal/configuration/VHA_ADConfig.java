package gov.va.aes.vear.gal.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.ldap.repository.config.EnableLdapRepositories;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

@Configuration
@EnableLdapRepositories(basePackages = "gov.va.aes.vear.gal.repositories", ldapTemplateRef="vhaLdapTemplate")
public class VHA_ADConfig {

    @Autowired
    Environment env;

    @Bean
    public LdapContextSource vhaContextSource() {
        LdapContextSource contextSource= new LdapContextSource();
        contextSource.setUrl(env.getRequiredProperty("ad.url"));
        contextSource.setBase("dc=vha,dc=med,dc=va,dc=gov");
        contextSource.setUserDn(env.getRequiredProperty("ad.service.user"));
        contextSource.setPassword(env.getRequiredProperty("ad.secret"));
        contextSource.setReferral("follow");
        return contextSource;
    }

    @Bean(name="vhaLdapTemplate")
    public LdapTemplate vhaLdapTemplate() {
        return new LdapTemplate(vhaContextSource());        
    }
}