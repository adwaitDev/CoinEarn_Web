package com.adwait.aitrading.service;

import com.adwait.aitrading.domain.VerificationType;
import com.adwait.aitrading.model.User;
import com.adwait.aitrading.model.VerificationCode;
import com.adwait.aitrading.repository.VerificationCodeRepository;
import com.adwait.aitrading.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {

    private final VerificationCodeRepository verificationCodeRepository;

    @Autowired
    public VerificationCodeServiceImpl(VerificationCodeRepository verificationCodeRepository) {
        this.verificationCodeRepository = verificationCodeRepository;
    }


    @Override
    public VerificationCode sendVerificationCode(User user, VerificationType verificationType) {

        VerificationCode verifyCode = new VerificationCode();
        verifyCode.setOtp(OtpUtils.generateOtp());
        verifyCode.setVerifyType(verificationType);
        verifyCode.setUser(user);

        return verificationCodeRepository.save(verifyCode);
    }

    @Override
    public VerificationCode getVerificationCodeById(Long id) throws Exception {
        Optional<VerificationCode> verificatnCode = verificationCodeRepository.findById(id);

        if(verificatnCode.isEmpty()){
            throw new Exception("Verification code not found.");
        }
        return verificatnCode.get();

    }

    @Override
    public VerificationCode getVerificationCodeByUser(User user) {

        //return verify_code_repo.findByUserId(user_id);
        return verificationCodeRepository.findByUserId(user.getId());
    }

    @Override
    public Boolean VerifyOtp(String otp, VerificationCode verificationCode) {
        return otp.equals(verificationCode.getOtp());
    }

    @Override
    public void deleteVerificationCodeById(VerificationCode verificationCode) {

        verificationCodeRepository.delete(verificationCode);
    }
}
