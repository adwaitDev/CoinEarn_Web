package com.adwait.aitrading.service;

import com.adwait.aitrading.model.PaymentDetails;
import com.adwait.aitrading.model.User;

public interface PaymentDetailsService {

    PaymentDetails addPaymentDetails(String accountNumber, String accountHolderName,
                                     String ifscCode, String bankName,
                                     User user);

    PaymentDetails getUsersPaymentDetails(User user);
}
