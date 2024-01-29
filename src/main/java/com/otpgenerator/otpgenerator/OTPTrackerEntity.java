package com.otpgenerator.otpgenerator;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "otp_tracker")
@NoArgsConstructor
public class OTPTrackerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long otpTrackerId;

    private String otp;

    private String emailId;

    private LocalDateTime generatedTime;

    private LocalDateTime expiryTime;

    @Enumerated(EnumType.STRING)
    private OTPStatus otpStatus;
}

