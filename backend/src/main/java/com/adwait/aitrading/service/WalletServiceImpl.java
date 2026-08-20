package com.adwait.aitrading.service;

import com.adwait.aitrading.domain.OrderType;
import com.adwait.aitrading.exception.WalletException;


import com.adwait.aitrading.model.Order;
import com.adwait.aitrading.model.User;
import com.adwait.aitrading.model.Wallet;
import com.adwait.aitrading.model.WalletTransaction;
import com.adwait.aitrading.repository.WalletRepository;
import com.adwait.aitrading.repository.WalletTransactionRepository;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class WalletServiceImpl implements WalletService{

    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;

    public WalletServiceImpl(WalletRepository walletRepository,
                             WalletTransactionRepository walletTransactionRepository){
        this.walletRepository = walletRepository;
        this.walletTransactionRepository = walletTransactionRepository;
    }

    public Wallet generateWallet(User user) {
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet getUserWallet(User user) {
        Wallet wallet = walletRepository.findByUserId(user.getId());
        if(wallet != null){
            return wallet;
        }
        wallet = generateWallet(user);
        return wallet;
    }

    @Override
    public Wallet findWalletById(Long id) throws WalletException {
        Optional<Wallet> wallet = walletRepository.findById(id);
        if(wallet.isPresent()){
            return wallet.get();
        }
        throw new WalletException("Wallet not found with Id: "+ id);
    }

    @Override
    public Wallet addBalance(Wallet wallet, Long money) {
        BigDecimal balance = wallet.getBalance();
        BigDecimal newBalance = balance.add(BigDecimal.valueOf(money));

        wallet.setBalance(newBalance);
        //return walletRepository.save(wallet);
        walletRepository.save(wallet);
        System.out.println("Updated wallet: "+ wallet);
        return wallet;
    }

    @Override
    public Wallet walletToWalletTransfer(User sender, Wallet receiverWallet, Long amount) throws WalletException {
        Wallet senderWallet = getUserWallet(sender);

        if(senderWallet.getBalance().compareTo(BigDecimal.valueOf(amount)) < 0){
            throw new WalletException("Insufficient balance.");
        }
        BigDecimal senderBalance = senderWallet.getBalance()
                .subtract(BigDecimal.valueOf(amount));

        senderWallet.setBalance(senderBalance);
        walletRepository.save(senderWallet);

        BigDecimal receiverBalance = receiverWallet.getBalance()
                .add(BigDecimal.valueOf(amount));

        receiverWallet.setBalance(receiverBalance);
        walletRepository.save(receiverWallet);
        return senderWallet;
    }

    @Override
    public Wallet payOrderPayment(Order order, User user) throws WalletException {
        Wallet wallet = getUserWallet(user);

        WalletTransaction walletTransaction = new WalletTransaction();
        walletTransaction.setWallet(wallet);
        walletTransaction.setPurpose(order.getOrderType()+ " " + order.getOrderItem().getCoin().getId() );

        walletTransaction.setDate(LocalDate.now());
        walletTransaction.setTransferId(order.getOrderItem().getCoin().getSymbol());

        if(order.getOrderType().equals(OrderType.BUY)){

            walletTransaction.setAmount(-order.getPrice().longValue());
            BigDecimal newBalance = wallet.getBalance().subtract(order.getPrice());

            if(newBalance.compareTo(order.getPrice()) < 0){
                System.out.println("inside---------- ");
                throw new WalletException("Insufficient funds for this transaction.");
            }
            System.out.println("outside---------- ");
            wallet.setBalance(newBalance);
        }

        else if(order.getOrderType().equals(OrderType.SELL)){

            walletTransaction.setAmount(order.getPrice().longValue());
            BigDecimal newBalance = wallet.getBalance().add(order.getPrice());

            wallet.setBalance(newBalance);
        }

        walletTransactionRepository.save(walletTransaction);
        walletRepository.save(wallet);
        return wallet;
    }
}
