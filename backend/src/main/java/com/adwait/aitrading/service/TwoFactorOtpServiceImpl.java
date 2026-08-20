package com.adwait.aitrading.service;

import com.adwait.aitrading.model.TwoFactorOtp;
import com.adwait.aitrading.model.User;
import com.adwait.aitrading.repository.TwoFactorOtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TwoFactorOtpServiceImpl implements TwoFactorOtpService{

    private final TwoFactorOtpRepository twoFactorOtpRepository;

    @Autowired
    public TwoFactorOtpServiceImpl(TwoFactorOtpRepository twoFactorOtpRepository) {
        this.twoFactorOtpRepository = twoFactorOtpRepository;
    }

    @Override
    public TwoFactorOtp createTwoFactorOtp(User user, String otp, String jwt) {
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();

        TwoFactorOtp twoFactorOtp = new TwoFactorOtp();
        twoFactorOtp.setOtp(otp);
        twoFactorOtp.setJwt(jwt);
        twoFactorOtp.setId(uuidString);
        twoFactorOtp.setUser(user);

        return twoFactorOtpRepository.save(twoFactorOtp);

    }

    @Override
    public TwoFactorOtp findByUser(Long userId) {

        return twoFactorOtpRepository.findByUserId(userId);
    }

    @Override
    public TwoFactorOtp findById(String id) {
        Optional<TwoFactorOtp> opt = twoFactorOtpRepository.findById(id);
        return opt.orElse(null);
    }

    @Override
    public boolean verifyTwoFactorOtp(TwoFactorOtp twoFactorOtp, String otp) {
        return twoFactorOtp.getOtp().equals(otp);
    }

    @Override
    public void deleteTwoFactorOtp(TwoFactorOtp twoFactorOtp) {

        twoFactorOtpRepository.delete(twoFactorOtp);
    }
}
