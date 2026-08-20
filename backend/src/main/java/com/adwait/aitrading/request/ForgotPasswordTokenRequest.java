package com.adwait.aitrading.request;

import com.adwait.aitrading.domain.VerificationType;
import lombok.Data;

@Data
public class ForgotPasswordTokenRequest {

    private String sendTo;
    private VerificationType verifyType;
}
