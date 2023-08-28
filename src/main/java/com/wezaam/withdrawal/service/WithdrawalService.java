package com.wezaam.withdrawal.service;

import com.wezaam.withdrawal.exception.TransactionException;
import com.wezaam.withdrawal.model.*;
import com.wezaam.withdrawal.repository.WithdrawalRepository;
import com.wezaam.withdrawal.repository.WithdrawalScheduledRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class WithdrawalService {

    @Autowired
    private WithdrawalRepository withdrawalRepository;
    @Autowired
    private WithdrawalScheduledRepository withdrawalScheduledRepository;
    @Autowired
    private WithdrawalProcessingService withdrawalProcessingService;
    @Autowired
    private PaymentMethodService paymentMethodService;
    @Autowired
    private EventsService eventsService;
    @Autowired
    private UserService userService;

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public boolean isValidWithdrawalUser(Long userId) {
        try {
            userService.findById(userId);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isValidWithdrawalPaymentMethod(Long paymentMethodId) {
        try {
            paymentMethodService.findById(paymentMethodId);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void handleWithdrawal(Withdrawal withdrawal) throws TransactionException {
        if (!isValidWithdrawalUser(withdrawal.getUserId())) { throw new TransactionException("User does not exist!");}
        if (!isValidWithdrawalPaymentMethod(withdrawal.getPaymentMethodId())) { throw new TransactionException("Invalid payment method!");}
        switch (withdrawal) {
            case WithdrawalInstant iw -> create(iw);
            case WithdrawalScheduled ws -> schedule(ws);
            default -> throw new IllegalStateException("Unexpected value: " + withdrawal);
        }
    }

    public void create(WithdrawalInstant withdrawalInstant) {
        WithdrawalInstant savedWithdrawalInstant = withdrawalRepository.save(withdrawalInstant);

        executorService.submit(() -> {
                try {
                    var transactionId = withdrawalProcessingService.sendToProcessing(withdrawalInstant.getAmount(), paymentMethodService.findById(withdrawalInstant.getPaymentMethodId()), userService.findById(withdrawalInstant.getUserId()).getMaxWithdrawalAmount());
                    savedWithdrawalInstant.setStatus(WithdrawalStatus.PROCESSING);
                    savedWithdrawalInstant.setTransactionId(transactionId);
                } catch (Exception e) {
                    if (e instanceof TransactionException) {
                        savedWithdrawalInstant.setStatus(WithdrawalStatus.FAILED);
                    } else {
                        savedWithdrawalInstant.setStatus(WithdrawalStatus.INTERNAL_ERROR);
                    }
                } finally {
                    withdrawalRepository.save(savedWithdrawalInstant);
                    eventsService.send(savedWithdrawalInstant);
                }
        });
    }

    public void schedule(WithdrawalScheduled withdrawalScheduled) {
        withdrawalScheduledRepository.save(withdrawalScheduled);
    }

    @Scheduled(fixedDelay = 5000)
    public void run() {
        withdrawalScheduledRepository.findAllByExecuteAtBeforeAndStatusEquals(Instant.now(), WithdrawalStatus.PENDING)
                .forEach(this::processScheduled);
    }

    private void processScheduled(WithdrawalScheduled withdrawal) {
        executorService.submit(() -> {
            try {
                var transactionId = withdrawalProcessingService.sendToProcessing(withdrawal.getAmount(), paymentMethodService.findById(withdrawal.getPaymentMethodId()), userService.findById(withdrawal.getUserId()).getMaxWithdrawalAmount());
                withdrawal.setStatus(WithdrawalStatus.PROCESSING);
                withdrawal.setTransactionId(transactionId);
            } catch (Exception e) {
                if (e instanceof TransactionException) {
                    withdrawal.setStatus(WithdrawalStatus.FAILED);
                } else {
                    withdrawal.setStatus(WithdrawalStatus.INTERNAL_ERROR);
                }
            } finally {
                withdrawalScheduledRepository.save(withdrawal);
                eventsService.send(withdrawal);
            }
        });
    }
    public List<WithdrawalInstant> findAllWithdrawals() {
        return withdrawalRepository.findAll();
    }

    public List<WithdrawalScheduled> findAllWithdrawalsScheduled() {
        return withdrawalScheduledRepository.findAll();
    }
}