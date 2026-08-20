package com.adwait.aitrading.controller;

import com.adwait.aitrading.exception.UserException;
import com.adwait.aitrading.model.User;
import com.adwait.aitrading.model.VerificationCode;
import com.adwait.aitrading.service.EmailService;
import com.adwait.aitrading.service.UserService;
import com.adwait.aitrading.service.VerificationCodeService;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VerificationController {

    private final VerificationCodeService verificationCodeService;
    private final UserService userService;

    // @Autowired
    // private EmailService emailService;

    @Autowired
    public VerificationController(VerificationCodeService verificationService, UserService userService) {
        this.verificationCodeService = verificationService;
        this.userService = userService;
    }
}
