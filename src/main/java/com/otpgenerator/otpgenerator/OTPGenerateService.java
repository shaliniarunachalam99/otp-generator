package com.otpgenerator.otpgenerator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@Slf4j
public class OTPGenerateService {

    @Value("${app.otp-generator.length}")
    private int otpLength;

    private OTPRepository otpRepository;

    private static final String POSSIBLE_NUMBERS = "0123456789";

    private static final int CONFIGURED_TIME_DIFFERENCE_MINUTES = 30;

    public OTPGenerateService(OTPRepository otpRepository) {
        this.otpRepository = otpRepository;
    }

    public OTPTrackerEntity getOtpForEmailId(String emailId) {
        String otp = getGeneratedOTP();
        LocalDateTime otpGeneratedTime = LocalDateTime.now();
        return saveOtpAndEmailToDB(emailId, otp, otpGeneratedTime);
    }

    private String getGeneratedOTP() {
        Random randomValue = new Random();
        char[] generatedOTP = new char[otpLength];
        for (int i = 0; i < otpLength; i++) {
            generatedOTP[i] = POSSIBLE_NUMBERS.charAt(randomValue.nextInt(POSSIBLE_NUMBERS.length()));
        }
        return new String(generatedOTP);
    }

    public OTPTrackerEntity saveOtpAndEmailToDB(String emailId, String otp, LocalDateTime otpGeneratedTime){

        OTPTrackerEntity otpTrackerEntity = new OTPTrackerEntity();
        otpTrackerEntity.setEmailId(emailId);
        otpTrackerEntity.setOtp(otp);
        otpTrackerEntity.setOtpStatus(OTPStatus.VALID);
        otpTrackerEntity.setGeneratedTime(otpGeneratedTime);

        return otpRepository.save(otpTrackerEntity);
    }

    public boolean validateUserOtp(String userEmailId, String userOtp){
        OTPTrackerEntity otpTrackerResponse = otpRepository.findByEmailIdAndOtpAndOtpStatus(userEmailId, userOtp, OTPStatus.VALID);
        boolean isOtpValid = false;
        if(otpTrackerResponse != null){
            isOtpValid = true;
        }
        return isOtpValid;
    }

    public void invalidateExpiryToken(OTPTrackerEntity otpTrackerEntity){

        LocalDateTime otpGeneratedTime = otpTrackerEntity.getGeneratedTime();

        boolean isWithinConfigurableTime = isWithinConfigurableTime(otpGeneratedTime);

        if (isWithinConfigurableTime) {
            log.info("Token is valid");
        } else {
            otpTrackerEntity.setOtpStatus(OTPStatus.EXPIRED);
            otpTrackerEntity.setExpiryTime(LocalDateTime.now());
            otpRepository.save(otpTrackerEntity);
        }
    }

    private static boolean isWithinConfigurableTime(LocalDateTime givenDateTime) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        Duration duration = Duration.between(currentDateTime, givenDateTime);
        long minutesDifference = Math.abs(duration.toMinutes());
        return minutesDifference <= CONFIGURED_TIME_DIFFERENCE_MINUTES;
    }
}
