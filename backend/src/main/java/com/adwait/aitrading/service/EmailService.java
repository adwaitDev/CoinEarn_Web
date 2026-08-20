package com.adwait.aitrading.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    // 1. Mark dependency as final to ensure immutability
    private final JavaMailSender mailSender;

    // 2. Use Constructor Injection (Spring automatically injects this)
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationOtpEmail(String email, String otp) throws MessagingException, MailSendException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");

        String subject = "Account verification";
        String text = "Your account verification code is: " + otp;

        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(text, true);
        mimeMessageHelper.setTo(email);

        try{
            mailSender.send(mimeMessage);
        }
        catch (MailException e){
            // throw new MailSendException(e.getMessage());
            throw new MailSendException("Failed to send email.");
        }
    }

}
