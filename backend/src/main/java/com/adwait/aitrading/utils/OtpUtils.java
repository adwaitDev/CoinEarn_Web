package com.adwait.aitrading.utils;

import java.util.Random;

public class OtpUtils {

    public static String generateOtp(){
        int otp_length = 6;
        Random random_otp = new Random();

        StringBuilder otp = new StringBuilder(otp_length);

        for (int i = 0; i < otp_length; i++){
            otp.append(random_otp.nextInt(10));
        }
        return otp.toString();
    }
}
