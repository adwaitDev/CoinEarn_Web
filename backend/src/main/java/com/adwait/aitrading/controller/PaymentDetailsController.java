package com.adwait.aitrading.controller;

import com.adwait.aitrading.exception.UserException;
import com.adwait.aitrading.model.PaymentDetails;
import com.adwait.aitrading.model.User;
import com.adwait.aitrading.service.PaymentDetailsService;
import com.adwait.aitrading.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PaymentDetailsController {

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentDetailsService paymentDetailsService;

    @PostMapping("/payment-details")
    public ResponseEntity<PaymentDetails> addPaymentDetails(@RequestBody PaymentDetails paymentDetailsRequest,
                                                            @RequestHeader("Authorizaton") String jwt) throws UserException {

        User user = userService.findUserProfileByJwt(jwt);

        PaymentDetails paymentDetails = paymentDetailsService.addPaymentDetails(
                paymentDetailsRequest.getAccountNumber(),
                paymentDetailsRequest.getAccountHolderName(),
                paymentDetailsRequest.getIfsc(),
                paymentDetailsRequest.getBankName(),
                user
        );

        return new ResponseEntity<>(paymentDetails, HttpStatus.CREATED);
    }

    @GetMapping("/payment-details")
    public ResponseEntity<PaymentDetails> getUsersPaymentDetails(@RequestHeader("Authorization") String jwt) throws UserException{

        User user = userService.findUserProfileByJwt(jwt);

        PaymentDetails payment_details = paymentDetailsService.getUsersPaymentDetails(user);

        return new ResponseEntity<>(payment_details, HttpStatus.CREATED);
    }
}
