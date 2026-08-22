package com.adwait.aitrading.model;

import com.adwait.aitrading.domain.UserRole;
import com.adwait.aitrading.domain.UserStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import lombok.Data;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String email;
    private String mobile;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserStatus status= UserStatus.PENDING;

    private boolean isVerified = false;

    @Embedded
    private  TwoFactorAuth twoFactorAuth = new TwoFactorAuth();

    private String picture;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.ROLE_USER;
}
