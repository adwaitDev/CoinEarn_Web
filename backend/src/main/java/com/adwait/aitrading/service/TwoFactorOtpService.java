package com.adwait.aitrading.service;

import com.adwait.aitrading.model.TwoFactorOtp;
import com.adwait.aitrading.model.User;

public interface TwoFactorOtpService {
    TwoFactorOtp createTwoFactorOtp(User user, String otp, String jwt);
    TwoFactorOtp findByUser(Long userId);
    TwoFactorOtp findById(String id);

    boolean verifyTwoFactorOtp(TwoFactorOtp twoFactorOtp, String otp);
    void deleteTwoFactorOtp(TwoFactorOtp twoFactorOtp);
}
