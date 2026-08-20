package com.adwait.aitrading.repository;

import com.adwait.aitrading.model.Coin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinRepository extends JpaRepository<Coin, String> {
}
