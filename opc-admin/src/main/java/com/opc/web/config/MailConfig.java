package com.opc.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * 邮件配置类
 * 如果application.yml中配置了邮件信息，则自动创建JavaMailSender Bean
 * 如果没有配置，则创建一个默认的Bean，需要在使用时手动配置SMTP
 *
 * @author opc
 */
@Configuration
public class MailConfig {

    @Value("${spring.mail.host:}")
    private String host;

    @Value("${spring.mail.port:587}")
    private int port;

    @Value("${spring.mail.username:}")
    private String username;

    @Value("${spring.mail.password:}")
    private String password;

    @Value("${spring.mail.default-encoding:UTF-8}")
    private String defaultEncoding;

    /**
     * 创建默认的JavaMailSender Bean
     * 当application.yml中没有配置spring.mail.host时使用
     */
    @Bean
    @ConditionalOnProperty(prefix = "spring.mail", name = "host", havingValue = "", matchIfMissing = true)
    public JavaMailSender defaultMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        // 设置默认值，使用时需要通过自定义SMTP方法重新配置
        mailSender.setHost("smtp.example.com");
        mailSender.setPort(587);
        mailSender.setUsername("your-email@example.com");
        mailSender.setPassword("your-password");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");
        props.put("mail.smtp.writetimeout", "10000");
        props.put("mail.debug", "false");

        return mailSender;
    }

    /**
     * 创建配置好的JavaMailSender Bean
     * 当application.yml中配置了spring.mail.host时使用
     */
    @Bean
    @Primary
    @ConditionalOnProperty(prefix = "spring.mail", name = "host", matchIfMissing = false)
    public JavaMailSender configuredMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        // 从配置文件中读取邮件配置
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        mailSender.setDefaultEncoding(defaultEncoding);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.required", "true");
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");
        props.put("mail.smtp.writetimeout", "10000");

        return mailSender;
    }
}
