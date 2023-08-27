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
import java.util.Optional;
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
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isValidWithdrawalPaymentMethod(Long paymentId) {
        try {
            paymentMethodService.findById(paymentId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void handleWithdrawal(Withdrawal withdrawal) {
        switch (withdrawal) {
            case InstantWithdrawal iw -> create(iw);
            case WithdrawalScheduled ws -> schedule(ws);
            default -> throw new IllegalStateException("Unexpected value: " + withdrawal);
        }
    }

    public void create(InstantWithdrawal instantWithdrawal) {
        InstantWithdrawal pendingInstantWithdrawal = withdrawalRepository.save(instantWithdrawal);

        executorService.submit(() -> {
            Optional<InstantWithdrawal> savedWithdrawalOptional = withdrawalRepository.findById(pendingInstantWithdrawal.getId());

            PaymentMethod paymentMethod;
            if (savedWithdrawalOptional.isPresent()) {
                paymentMethod = paymentMethodService.findById(savedWithdrawalOptional.get().getPaymentMethodId());
            } else {
                paymentMethod = null;
            }

            if (savedWithdrawalOptional.isPresent() && paymentMethod != null) {
                InstantWithdrawal savedInstantWithdrawal = savedWithdrawalOptional.get();
                try {
                    var transactionId = withdrawalProcessingService.sendToProcessing(instantWithdrawal.getAmount(), paymentMethod);
                    savedInstantWithdrawal.setStatus(WithdrawalStatus.PROCESSING);
                    savedInstantWithdrawal.setTransactionId(transactionId);
                    withdrawalRepository.save(savedInstantWithdrawal);
                    eventsService.send(savedInstantWithdrawal);
                } catch (Exception e) {
                    if (e instanceof TransactionException) {
                        savedInstantWithdrawal.setStatus(WithdrawalStatus.FAILED);
                        withdrawalRepository.save(savedInstantWithdrawal);
                        eventsService.send(savedInstantWithdrawal);
                    } else {
                        savedInstantWithdrawal.setStatus(WithdrawalStatus.INTERNAL_ERROR);
                        withdrawalRepository.save(savedInstantWithdrawal);
                        eventsService.send(savedInstantWithdrawal);
                    }
                }
            }
        });
    }

    public void schedule(WithdrawalScheduled withdrawalScheduled) {
        withdrawalScheduledRepository.save(withdrawalScheduled);
    }

    @Scheduled(fixedDelay = 5000)
    public void run() {
        withdrawalScheduledRepository.findAllByExecuteAtBefore(Instant.now())
                .forEach(this::processScheduled);
    }

    private void processScheduled(WithdrawalScheduled withdrawal) {
        PaymentMethod paymentMethod = paymentMethodService.findById(withdrawal.getPaymentMethodId());
        if (paymentMethod != null) {
            try {
                var transactionId = withdrawalProcessingService.sendToProcessing(withdrawal.getAmount(), paymentMethod);
                withdrawal.setStatus(WithdrawalStatus.PROCESSING);
                withdrawal.setTransactionId(transactionId);
                withdrawalScheduledRepository.save(withdrawal);
                eventsService.send(withdrawal);
            } catch (Exception e) {
                if (e instanceof TransactionException) {
                    withdrawal.setStatus(WithdrawalStatus.FAILED);
                    withdrawalScheduledRepository.save(withdrawal);
                    eventsService.send(withdrawal);
                } else {
                    withdrawal.setStatus(WithdrawalStatus.INTERNAL_ERROR);
                    withdrawalScheduledRepository.save(withdrawal);
                    eventsService.send(withdrawal);
                }
            }
        }
    }
    public List<InstantWithdrawal> findAllWithdrawals() {
        return withdrawalRepository.findAll();
    }

    public List<WithdrawalScheduled> findAllWithdrawalsScheduled() {
        return withdrawalScheduledRepository.findAll();
    }
}
