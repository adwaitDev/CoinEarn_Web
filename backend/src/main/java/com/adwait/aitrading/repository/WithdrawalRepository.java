package com.adwait.aitrading.repository;

// import com.adwait.aitrading.domain.WithdrawalStatus;
import com.adwait.aitrading.model.Withdrawal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WithdrawalRepository extends JpaRepository<Withdrawal, Long> {

    List<Withdrawal> findByUserId(Long userId);
}
