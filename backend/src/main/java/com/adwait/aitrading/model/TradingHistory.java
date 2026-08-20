package com.adwait.aitrading.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class TradingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double sellingPrice;

    private double buyingPrice;

    @ManyToOne
    private Coin coin;

    @ManyToOne
    private User user;
}
