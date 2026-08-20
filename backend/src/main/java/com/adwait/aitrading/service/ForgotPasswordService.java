package com.adwait.aitrading.service;

import com.adwait.aitrading.domain.VerificationType;
import com.adwait.aitrading.model.ForgotPasswordToken;
import com.adwait.aitrading.model.User;

public interface ForgotPasswordService {
    ForgotPasswordToken createToken(User user,
                                    String id, String otp,
                                    VerificationType verifyType,
                                    String sendTo);

    ForgotPasswordToken findById(String id);

    ForgotPasswordToken findByUser(Long userId);

    void deleteToken(ForgotPasswordToken token);

    boolean verifyToken(ForgotPasswordToken token, String otp);
}
