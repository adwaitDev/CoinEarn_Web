package com.adwait.aitrading.controller;

import com.adwait.aitrading.exception.UserException;
import com.adwait.aitrading.request.ForgotPasswordTokenRequest;
import com.adwait.aitrading.domain.VerificationType;
import com.adwait.aitrading.model.ForgotPasswordToken;
import com.adwait.aitrading.model.User;
import com.adwait.aitrading.model.VerificationCode;
import com.adwait.aitrading.request.ResetPasswordRequest;
import com.adwait.aitrading.response.ApiResponse;
import com.adwait.aitrading.response.AuthResponse;
import com.adwait.aitrading.service.EmailService;
import com.adwait.aitrading.service.ForgotPasswordService;
import com.adwait.aitrading.service.UserService;
import com.adwait.aitrading.service.VerificationCodeService;
import com.adwait.aitrading.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @Autowired
    private EmailService emailService;


    @GetMapping("/api/users/profile")
    public ResponseEntity<User> getUserProfileHandler(@RequestHeader("Authorization") String jwt) throws UserException {

        User user = userService.findUserProfileByJwt(jwt);
        // This was added here
        user.setPassword(null);

        //return new ResponseEntity<User>(user, HttpStatus.OK);
        return new ResponseEntity<User>(user, HttpStatus.ACCEPTED);
    }

    @GetMapping("/api/users/{userId}")
    public ResponseEntity<User> findUserById(
            @PathVariable Long userId,
            @RequestHeader("Authorization") String jwt) throws UserException {

        User user = userService.findUserProfileById(userId);
        user.setPassword(null);

        return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
    }

    @GetMapping("/api/users/email/{email}")
    public ResponseEntity<User> findUserByEmail(
            @PathVariable String email,
            @RequestHeader("Authorization") String jwt) throws UserException {

        User user = userService.findUserProfileByEmail(email);

        return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
    }

    @PatchMapping("/api/users/enable-two-factor/verify-otp/{otp}")
    public ResponseEntity<User> enableTwoFactorAuthentication(@PathVariable String otp,
                                                              @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user);

        String sendTo = verificationCode.getVerifyType().equals(VerificationType.EMAIL)?
                verificationCode.getEmail(): verificationCode.getMobile();

        // This was here before.
        //boolean is_verified = verification_code.getOtp().equals(otp);
        boolean isVerified = verificationCodeService.VerifyOtp(otp, verificationCode);


        if(isVerified){
            User updatedUser = userService.enableTwoFactorAuthentication(
                    verificationCode.getVerifyType(), sendTo, user);

            verificationCodeService.deleteVerificationCodeById(verificationCode);

            //return new ResponseEntity<>(updatedUser, HttpStatus.OK);
            return ResponseEntity.ok(updatedUser);
        }
        throw new Exception("Invalid otp.");
    }

    @PostMapping("/api/users/verification/{verificationType}/send-otp")
    public ResponseEntity<String> sendVerificationOtp(
            @RequestHeader("Authorization") String jwt,
            @PathVariable VerificationType verificationType) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user);

        if(verificationCode == null){

            verificationCode = verificationCodeService
                    .sendVerificationCode(user, verificationType);
        }
        if(verificationType.equals(VerificationType.EMAIL)){
            emailService.sendVerificationOtpEmail(user.getEmail(), verificationCode.getOtp());
        }

        return new ResponseEntity<>("Verification otp sent successfully", HttpStatus.OK);
        // The below one is new.
        // return ResponseEntity.ok("Verification OTP sent successfully.");
    }

    @PatchMapping("/api/users/verification/verify-otp/{otp}")
    public ResponseEntity<User> verifyOTP(
            @RequestHeader("Authorization") String jwt,
            @PathVariable String otp
    ) throws Exception {


        User user = userService.findUserProfileByJwt(jwt);

        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user);


        boolean isVerified = verificationCodeService.VerifyOtp(otp, verificationCode);

        if (isVerified) {
            verificationCodeService.deleteVerificationCodeById(verificationCode);
            User verifiedUser = userService.verifyUser(user);
            return ResponseEntity.ok(verifiedUser);
        }
        throw new Exception("Wrong otp");
    }



    @PostMapping("/auth/users/reset-password/send-otp")
    public ResponseEntity<AuthResponse> sendForgotPasswordOtp(

            @RequestBody ForgotPasswordTokenRequest req) throws Exception {

        User user = userService.findUserProfileByEmail(req.getSendTo());
        String otp = OtpUtils.generateOtp();
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();

        ForgotPasswordToken token =  forgotPasswordService.findByUser(user.getId());

        if(token == null){
            token = forgotPasswordService.createToken(user, uuidString,
                    otp, req.getVerifyType(), req.getSendTo());
        }

        if(req.getVerifyType().equals(VerificationType.EMAIL)){
            emailService.sendVerificationOtpEmail(user.getEmail(), token.getOtp());
        }
        AuthResponse response = new AuthResponse();
        response.setSession(token.getId());
        response.setMessage("Password reset otp sent successfully");

        // This was here before
        //return new ResponseEntity<>(response, HttpStatus.OK);
        return ResponseEntity.ok(response);
    }


    @PatchMapping("/auth/users/reset-password/verify-otp")
    public ResponseEntity<ApiResponse> resetPassword(@RequestParam String id,
                                                     @RequestBody ResetPasswordRequest req
            /*,@RequestHeader("Authorization") String jwt*/) throws Exception {


        ForgotPasswordToken forgotPasswordToken = forgotPasswordService.findById(id);

        // This was here before
        //boolean isVerified = forgotPasswordToken.equals(req.getOtp());
        boolean isVerified = forgotPasswordService.verifyToken(forgotPasswordToken, req.getOtp());


        if(isVerified){
            userService.updatePassword(forgotPasswordToken.getUser(), req.getPassword());
            ApiResponse res = new ApiResponse();
            res.setMessage("Password updated successfully");

            // This was here before.
            //return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
            return ResponseEntity.ok(res);
        }
        throw new Exception("Wrong otp");
    }

}
