package com.otpgenerator.otpgenerator;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OTPRepository extends JpaRepository <OTPTrackerEntity, Long>{

    OTPTrackerEntity findByEmailIdAndOtpAndOtpStatus(String emailId, String otp, OTPStatus otpStatus);

    List<OTPTrackerEntity> findByOtpStatus(OTPStatus otpStatus);
}
