package com.adwait.aitrading.service;

import com.adwait.aitrading.domain.WithdrawalStatus;
import com.adwait.aitrading.model.User;
import com.adwait.aitrading.model.Withdrawal;
import com.adwait.aitrading.repository.WithdrawalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WithdrawalServiceImpl implements WithdrawalService{

    private final WithdrawalRepository withdrawalRepository;

    public WithdrawalServiceImpl(WithdrawalRepository withdrawalRepository) {
        this.withdrawalRepository = withdrawalRepository;
    }

    @Override
    public Withdrawal requestWithdrawal(Long amount, User user) {
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setAmount(amount);
        withdrawal.setUser(user);
        withdrawal.setStatus(WithdrawalStatus.PENDING);
        withdrawal.setDate(LocalDateTime.now());

        return withdrawalRepository.save(withdrawal);
    }

    @Override
    public Withdrawal proceedWithdrawal(Long withdrawalId, boolean accept) throws Exception {

        Optional<Withdrawal> withdrawalOptional = withdrawalRepository.findById(withdrawalId);

        if(withdrawalOptional.isEmpty()){
            //throw new Exception("Withdrawal not found...");
            throw new Exception("Withdrawal Id is incorrect.");
        }
        Withdrawal withdrawal = withdrawalOptional.get();

        withdrawal.setDate(LocalDateTime.now());

        if(accept){
            withdrawal.setStatus(WithdrawalStatus.SUCCESS);
        }
        else{
            //withdrawal.setStatus(WithdrawalStatus.PENDING);
            withdrawal.setStatus(WithdrawalStatus.DECLINE);
        }
        return withdrawalRepository.save(withdrawal);
    }

    @Override
    public List<Withdrawal> getUsersWithdrawalHistory(User user) {

        return withdrawalRepository.findByUserId(user.getId());
    }

    @Override
    public List<Withdrawal> getAllWithdrawalRequest() {

        return withdrawalRepository.findAll();
    }
}
