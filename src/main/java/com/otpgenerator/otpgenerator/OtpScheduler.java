package com.otpgenerator.otpgenerator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OtpScheduler {

    private OTPRepository otpRepository;

    public OtpScheduler(OTPRepository otpRepository, OTPGenerateService otpGenerateService) {
        this.otpRepository = otpRepository;
        this.otpGenerateService = otpGenerateService;
    }

    private OTPGenerateService otpGenerateService;

    @Value("${app.otp-generator.scheduler.invalidate-cron-enabled}")
    private Boolean enabledInvalidateOtp;

    @Scheduled(cron = "${app.otp-generator.scheduler.invalidate-cron}")
    public void schedulerToInvalidateOtp(){
        if(enabledInvalidateOtp){

            log.info("Started scheduler to invalidate OTP");

            List<OTPTrackerEntity> validOtpResponses = otpRepository.findByOtpStatus(OTPStatus.VALID);

            validOtpResponses.forEach(otpResponse -> otpGenerateService.invalidateExpiryToken(otpResponse));

            log.info("Completed scheduler job for invalidate OTP");
        }
    }
}
