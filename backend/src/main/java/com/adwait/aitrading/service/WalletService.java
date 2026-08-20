package com.adwait.aitrading.service;

import com.adwait.aitrading.exception.WalletException;
import com.adwait.aitrading.model.Order;
import com.adwait.aitrading.model.User;
import com.adwait.aitrading.model.Wallet;

public interface WalletService {

    Wallet getUserWallet(User user);
    Wallet addBalance(Wallet wallet, Long money);
    Wallet findWalletById(Long id) throws WalletException;
    Wallet walletToWalletTransfer(User sender, Wallet receiverWallet, Long amount) throws WalletException;
    Wallet payOrderPayment(Order order, User user) throws WalletException;

}
