package com.adwait.aitrading.service;

import com.adwait.aitrading.domain.WalletTransactionType;
import com.adwait.aitrading.model.Wallet;
import com.adwait.aitrading.model.WalletTransaction;
import com.adwait.aitrading.repository.WalletTransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
//import java.time.LocalDateTime;
import java.util.List;

@Service
public class WalletTransactionServiceImpl implements WalletTransactionService{

    private final WalletTransactionRepository walletTransactionRepository;

    public WalletTransactionServiceImpl(WalletTransactionRepository walletTransactionRepository) {
        this.walletTransactionRepository = walletTransactionRepository;
    }

    @Override
    public WalletTransaction createTransaction(Wallet wallet,
                                               WalletTransactionType type,
                                               String transferId,
                                               String purpose,
                                               Long amount
    ) {
        WalletTransaction transaction = new WalletTransaction();
        transaction.setWallet(wallet);
        transaction.setDate(LocalDate.now());
        transaction.setType(type);
        transaction.setTransferId(transferId);
        transaction.setPurpose(purpose);
        transaction.setAmount(amount);

        return walletTransactionRepository.save(transaction);
    }

    @Override
    public List<WalletTransaction> getTransactions(Wallet wallet, WalletTransactionType type) {
        return walletTransactionRepository.findByWalletOrderByDateDesc(wallet);
    }
}
