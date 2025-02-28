package com.postiy.postify.controller;

import com.postiy.postify.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class EmailController {
    @Autowired
    EmailService emailService;

    @GetMapping("/send-email")
    public  String sendEmail(@RequestParam String to, @RequestParam String subject,@RequestParam String body){
        emailService.sendEmail(to,subject,body);
        return "Email send successfully";
    }
}
