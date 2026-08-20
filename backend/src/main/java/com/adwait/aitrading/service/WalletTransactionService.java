package com.adwait.aitrading.service;

import com.adwait.aitrading.model.WalletTransaction;
import com.adwait.aitrading.model.Wallet;
import com.adwait.aitrading.domain.WalletTransactionType;

import java.util.List;
public interface WalletTransactionService {
    WalletTransaction createTransaction(Wallet wallet,
                                        WalletTransactionType type,
                                        String transferId,
                                        String purpose,
                                        Long amount
    );
    List<WalletTransaction> getTransactions(Wallet wallet, WalletTransactionType type);

}
