package com.adwait.aitrading.service;

import com.adwait.aitrading.domain.VerificationType;
import com.adwait.aitrading.model.ForgotPasswordToken;
import com.adwait.aitrading.model.User;
import com.adwait.aitrading.repository.ForgotPasswordRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ForgotPasswordServiceImpl implements ForgotPasswordService{
    private final ForgotPasswordRepository forgotPasswordRepository;

    // Spring automatically injects this because it's the only constructor
    public ForgotPasswordServiceImpl(ForgotPasswordRepository forgotPasswordRepository) {
        this.forgotPasswordRepository = forgotPasswordRepository;
    }

    @Override
    public ForgotPasswordToken createToken(User user, String id,
                                           String otp, VerificationType verifyType,
                                           String sendTo) {

        ForgotPasswordToken token = new ForgotPasswordToken();
        token.setUser(user);
        token.setSendTo(sendTo);
        token.setVerificationType(verifyType);
        token.setOtp(otp);
        token.setId(id);
        return forgotPasswordRepository.save(token);
    }

    @Override
    public ForgotPasswordToken findById(String id) {
        Optional<ForgotPasswordToken> token = forgotPasswordRepository.findById(id);
        return token.orElse(null);
    }

    @Override
    public ForgotPasswordToken findByUser(Long userId) {

        return forgotPasswordRepository.findByUserId(userId);
    }

    @Override
    public void deleteToken(ForgotPasswordToken token) {
        forgotPasswordRepository.delete(token);

    }

    @Override
    public boolean verifyToken(ForgotPasswordToken token, String otp) {
        return token.getOtp().equals(otp);
    }
}
