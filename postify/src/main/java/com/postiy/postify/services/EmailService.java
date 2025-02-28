package com.postiy.postify.services;

import com.postiy.postify.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    UserRepository userRepository;
 //   private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
 private static final String EMAIL_SUBJECT = "Discover Postify: Your Ultimate Blogging Platfrom";
    private static final String EMAIL_FROM = "thoratakshada96@gmail.com";

    @Async
    public void sendEmail(String to, String subject, String body) {
        //logger.info("Starting email sending process to {}...", to);
        log.info("Starting email sending process to {}....",to);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("thoratakshada96@gmail.com");
        mailSender.send(message);
       // logger.info("Email sent successfully to user {}!", to);
        log.info("Email sent seccessfully to user {}!",to);
    }

    @Async
  //  @Scheduled(fixedRate = 10000)
    public void sendPromotionalEmailsToAllUsers() {
       // logger.info("Starting bulk email sending process...");
        log.info("Starting bulk email sending process...");
        userRepository.findAll().forEach(user -> {
            String emailBody = "Dear " + user.getUsername() + ",\n\n" +
                    "Are you ready to take your blogging to the next level? Introducing Postify, the ultimate platform designed to support " +
                    "bloggers like you! Whether you're a seasoned writer or just starting out, Postify has everything you need to create, share, " +
                    "and grow your blog effortlessly.\n\n" +
                    "Happy Blogging,\nThe Postify Team";

            sendEmail(user.getEmail(), "Discover Postify: Your Ultimate Blogging Platform!", emailBody);
        });
       // logger.info("Bulk email sent successfully.");
        log.info("Bulk email sending successfully!");

    }
}
