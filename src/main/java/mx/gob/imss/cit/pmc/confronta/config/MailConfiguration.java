package mx.gob.imss.cit.pmc.confronta.config;

import mx.gob.imss.cit.pmc.confronta.repository.ParameterRepository;
import mx.gob.imss.cit.pmc.confronta.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfiguration {

    @Autowired
    private ParameterRepository parameterRepository;

    @Bean
    public JavaMailSender getMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        String host = StringUtils.getFromParam(parameterRepository.findOneByCve("config.mailHost"));
        String port = StringUtils.getFromParam(parameterRepository.findOneByCve("config.mailPort"));
        String ttlsEnable = StringUtils.getFromParam(parameterRepository.findOneByCve("config.mail.smtp.starttls.enable"));
        String ttlsRequired = StringUtils.getFromParam(parameterRepository.findOneByCve("config.mail.smtp.starttls.required"));
        String auth = StringUtils.getFromParam(parameterRepository.findOneByCve("config.mail.smtp.auth"));
        String protocol = StringUtils.getFromParam(parameterRepository.findOneByCve("config.mail.transport.protocol"));
        String quitwait = StringUtils.getFromParam(parameterRepository.findOneByCve("config.mail.smtp.starttls.quitwait"));
        String debug = StringUtils.getFromParam(parameterRepository.findOneByCve("config.mail.debug"));

        mailSender.setHost(host);
        mailSender.setPort(Integer.parseInt(port));

        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.starttls.enable", ttlsEnable);
        javaMailProperties.put("mail.smtp.starttls.required", ttlsRequired);
        javaMailProperties.put("mail.smtp.auth", auth);
        javaMailProperties.put("mail.transport.protocol", protocol);
        javaMailProperties.put("mail.smtp.starttls.quitwait", quitwait);
        javaMailProperties.put("mail.debug", debug);

        mailSender.setJavaMailProperties(javaMailProperties);
        return mailSender;
    }

}
