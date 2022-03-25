package com.wezaam.withdrawal.service;


import com.wezaam.withdrawal.domain.rest.request.WithdrawalListResponse;
import com.wezaam.withdrawal.exception.TransactionException;
import com.wezaam.withdrawal.model.*;
import com.wezaam.withdrawal.repository.PaymentMethodRepository;
import com.wezaam.withdrawal.repository.WithdrawalRepository;
import com.wezaam.withdrawal.repository.WithdrawalScheduledRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class WithdrawalServiceImpl implements WithdrawalService {

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

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public void create(Withdrawal withdrawal) {
        Withdrawal pendingWithdrawal = withdrawalRepository.save(withdrawal);

        executorService.submit(() -> {
            Optional<Withdrawal> savedWithdrawalOptional = withdrawalRepository.findById(pendingWithdrawal.getId());

            PaymentMethod paymentMethod;
            if (savedWithdrawalOptional.isPresent()) {
                paymentMethod = paymentMethodRepository.findById(savedWithdrawalOptional.get().getPaymentMethodId()).orElse(null);
            } else {
                paymentMethod = null;
            }

            if (savedWithdrawalOptional.isPresent() && paymentMethod != null) {
                Withdrawal savedWithdrawal = savedWithdrawalOptional.get();
                try {
                    Long transactionId = withdrawalProcessingService.sendToProcessing(withdrawal.getAmount(), paymentMethod);
                    savedWithdrawal.setStatus(WithdrawalStatus.PROCESSING);
                    savedWithdrawal.setTransactionId(transactionId);
                    withdrawalRepository.save(savedWithdrawal);
                    eventsService.send(savedWithdrawal);
                } catch (Exception e) {
                    if (e instanceof TransactionException) {
                        savedWithdrawal.setStatus(WithdrawalStatus.FAILED);
                        withdrawalRepository.save(savedWithdrawal);
                        eventsService.send(savedWithdrawal);
                    } else {
                        savedWithdrawal.setStatus(WithdrawalStatus.INTERNAL_ERROR);
                        withdrawalRepository.save(savedWithdrawal);
                        eventsService.send(savedWithdrawal);
                    }
                }
            }
        });
    }


    public void schedule(WithdrawalScheduled withdrawalScheduled) {
        withdrawalScheduledRepository.save(withdrawalScheduled);
    }

    @Override
    public WithdrawalListResponse findAll() {
        List<Withdrawal> withdrawals = withdrawalRepository.findAll();
        List<WithdrawalScheduled> withdrawalsScheduled = withdrawalScheduledRepository.findAll();
        List<Withdrawal> result = new ArrayList<>();

        WithdrawalListResponse withdrawalListResponse =
                new WithdrawalListResponse(withdrawals, withdrawalsScheduled);

        return withdrawalListResponse;
    }

    //@Scheduled(fixedDelay = 5000)
    private void run() {
        withdrawalScheduledRepository.findAllByExecuteAtBefore(Instant.now())
                .forEach(this::processScheduled);
    }

    private void processScheduled(WithdrawalScheduled withdrawal) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(withdrawal.getPaymentMethodId()).orElse(null);
        if (paymentMethod != null) {
            try {
                Long transactionId = withdrawalProcessingService.sendToProcessing(withdrawal.getAmount(), paymentMethod);
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

}
