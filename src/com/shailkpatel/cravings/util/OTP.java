package com.shailkpatel.cravings.util;

import java.security.SecureRandom;
import java.util.Objects;
import java.util.Random;

public class OTP {

    private String otp;

    public String generateOTP() {
        StringBuilder otp = new StringBuilder();
        int length = 4;

        SecureRandom random = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10); // 0 to 9
            otp.append(digit);
        }
        this.otp = otp.toString();
        return otp.toString();
    }

    public boolean verify(String otp){
        return Objects.equals(this.otp, otp);
    }
}
