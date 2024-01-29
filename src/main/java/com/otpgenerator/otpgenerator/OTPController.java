package com.otpgenerator.otpgenerator;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class OTPController {

    private OTPGenerateService otpGenerateService;

    @PostMapping("/otp/generate/{userEmailId}")
    public OTPTrackerEntity generateOtpForEmailId(@PathVariable String userEmailId){
        return otpGenerateService.getOtpForEmailId(userEmailId);
    }

    @PostMapping("/otp/validate/{userEmailId}/{otp}")
    public Boolean validateOtp(@PathVariable String userEmailId, @PathVariable String otp){
        return otpGenerateService.validateUserOtp(userEmailId, otp);
    }
}
