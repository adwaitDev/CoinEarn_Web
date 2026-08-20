package com.adwait.aitrading.repository;

import com.adwait.aitrading.model.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails, Long> {

    PaymentDetails findByUserId(Long userId);
    // PaymentDetails getPaymentDetailsByUserId(Long userId);
}
