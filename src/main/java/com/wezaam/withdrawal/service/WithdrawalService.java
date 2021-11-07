package com.wezaam.withdrawal.service;

import com.wezaam.withdrawal.exception.NoSuchPaymentMethodException;
import com.wezaam.withdrawal.exception.TransactionException;
import com.wezaam.withdrawal.model.*;
import com.wezaam.withdrawal.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class WithdrawalService {

    @Autowired
    private WithdrawalRepository withdrawalRepository;
    @Autowired
    private WithdrawalScheduledRepository withdrawalScheduledRepository;
    @Autowired
    private WithdrawalProcessingService withdrawalProcessingService;
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    @Autowired
    private EventsService eventsService;
    @Autowired
    private Clock clock;
    @Autowired
    private ExecutorService executorService;

    public List<Withdrawal> findAll() {
        List<Withdrawal> all = new ArrayList<>();
        all.addAll(withdrawalRepository.findAll());
        all.addAll(withdrawalScheduledRepository.findAll());
        return all;
    }

    public Withdrawal create(Withdrawal withdrawal) {
        withdrawal.setStatus(WithdrawalStatus.PENDING);
        withdrawal.setCreatedAt(Instant.now(clock));
        Withdrawal savedWithdrawal = withdrawalRepository.save(withdrawal);
        executorService.submit(() -> process(savedWithdrawal));
        return savedWithdrawal;
    }

    public WithdrawalScheduled schedule(WithdrawalScheduled withdrawalScheduled) {
        withdrawalScheduled.setStatus(WithdrawalStatus.PENDING);
        withdrawalScheduled.setCreatedAt(Instant.now(clock));
        return withdrawalScheduledRepository.save(withdrawalScheduled);
    }

    void process(Withdrawal withdrawal) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(withdrawal.getPaymentMethodId())
                .orElseThrow(() -> new NoSuchPaymentMethodException(withdrawal.getPaymentMethodId()));
        try {
            var transactionId = withdrawalProcessingService.sendToProcessing(withdrawal.getAmount(), paymentMethod);
            withdrawal.setStatus(WithdrawalStatus.PROCESSING);
            withdrawal.setTransactionId(transactionId);
        } catch (Exception e) {
            withdrawal.setStatus(e instanceof TransactionException ?
                    WithdrawalStatus.FAILED : WithdrawalStatus.INTERNAL_ERROR);
        } finally {
            withdrawalRepository.save(withdrawal);
        }
    }

    void sendEvent(Withdrawal withdrawal) {
        eventsService.send(withdrawal);
        withdrawal.setSentAt(Instant.now(clock));
        withdrawalRepository.save(withdrawal);
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }
}
