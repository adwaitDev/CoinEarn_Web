package com.adwait.aitrading.model;

import com.adwait.aitrading.domain.VerificationType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ForgotPasswordToken {

    @Id
    private String id;

    @OneToOne
    private User user;

    private String otp;

    @Enumerated(EnumType.STRING)
    private VerificationType verificationType;

    private String sendTo;
}
