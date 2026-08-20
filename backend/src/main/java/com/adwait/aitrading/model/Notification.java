package com.adwait.aitrading.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Notification {

    private Long fromUserId;
    private Long toUserId;
    private Long amount;
    private String message;
}
