package com.adwait.aitrading.service;

import com.adwait.aitrading.domain.VerificationType;
import com.adwait.aitrading.model.User;
import com.adwait.aitrading.model.VerificationCode;

public interface VerificationCodeService {
    VerificationCode sendVerificationCode(User user, VerificationType verificationType);

    VerificationCode getVerificationCodeById(Long id) throws Exception;

    //VerificationCode getVerificationCodeByUser(Long user_id);
    VerificationCode getVerificationCodeByUser(User user);
    Boolean VerifyOtp(String otp, VerificationCode verificationCode);

    void deleteVerificationCodeById(VerificationCode verificationCode);
}
