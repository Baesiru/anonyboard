package com.example.anonyboard.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {
    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private int port;
    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;
    @Value("${spring.mail.properties.protocol}")
    private String protocol;
    @Value("${spring.mail.properties.auth}")
    private String auth;
    @Value("${spring.mail.properties.socket}")
    private String socket;
    @Value("${spring.mail.properties.starttls}")
    private String starttls;
    @Value("${spring.mail.properties.debug}")
    private String debug;
    @Value("${spring.mail.properties.ssl.trust}")
    private String trust;
    @Value("${spring.mail.properties.protocols}")
    private String sslProtocol;

    @Bean
    public JavaMailSender mailSender() {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.transport.protocol", protocol);
        javaMailProperties.put("mail.smtp.auth", auth);
        javaMailProperties.put("mail.smtp.socketFactory.class", socket);
        javaMailProperties.put("mail.smtp.starttls.enable", starttls);
        javaMailProperties.put("mail.debug", debug);
        javaMailProperties.put("mail.smtp.ssl.trust", trust);
        javaMailProperties.put("mail.smtp.ssl.protocols", sslProtocol);

        mailSender.setJavaMailProperties(javaMailProperties);

        return mailSender;
    }
}
