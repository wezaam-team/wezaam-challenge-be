package com.wezaam.withdrawal.service;


import com.wezaam.withdrawal.domain.WithdrawalListResponse;
import com.wezaam.withdrawal.domain.WithdrawalProcessingResponse;
import com.wezaam.withdrawal.exception.TransactionException;
import com.wezaam.withdrawal.model.*;
import com.wezaam.withdrawal.repository.PaymentMethodRepository;
import com.wezaam.withdrawal.repository.UserRepository;
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
    private UserRepository userRepository;
    @Autowired
    private EventsService eventsService;

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public void create(Withdrawal withdrawal) {
        Withdrawal pendingWithdrawal = withdrawalRepository.save(withdrawal);
        executorService.submit(() -> {
            Optional<Withdrawal> savedWithdrawalOptional = withdrawalRepository.findById(pendingWithdrawal.getId());

            savedWithdrawalOptional.ifPresent( wp -> {
                WithdrawalProcessingResponse response = callToWithdrawalProcess(wp.getAmount(), wp.getPaymentMethodId(), wp.getUserId());
                wp.setStatus(response.getStatus());
                wp.setTransactionId(response.getTransactionId());
                withdrawalRepository.save(wp);
                response.setExecution(WithdrawalExecution.ASAP);
                response.setExecutedAt(Instant.now());
                eventsService.sendWithdrawalStatusMessage(response);
            });
        });
    }

    private WithdrawalProcessingResponse callToWithdrawalProcess(Double amount, Long paymentMethodId, Long userId) {
            WithdrawalProcessingResponse response = new WithdrawalProcessingResponse();
            Optional<PaymentMethod> paymentMethod = paymentMethodRepository.findById(paymentMethodId);
            Optional<User> user = userRepository.findById(userId);

            if (paymentMethod.isPresent() && user.isPresent()) {
                try {
                    Long transactionId = withdrawalProcessingService.sendToProcessing(amount, paymentMethod.get(), user.get());
                    response.setStatus(WithdrawalStatus.PROCESSING);
                    response.setTransactionId(transactionId);
                } catch (TransactionException te) {
                    te.printStackTrace();
                    response.setStatus(WithdrawalStatus.FAILED);
                } catch (Exception e) {
                    e.printStackTrace();
                    response.setStatus(WithdrawalStatus.INTERNAL_ERROR);
                }
                response.setPaymentMethod(paymentMethod.get());
                response.setUser(user.get());
            }
        return response;
    }

    public void schedule(WithdrawalScheduled withdrawalScheduled) {
        withdrawalScheduledRepository.save(withdrawalScheduled);
    }

    private void processScheduled(WithdrawalScheduled withdrawal) {
        WithdrawalProcessingResponse response = callToWithdrawalProcess(withdrawal.getAmount(), withdrawal.getPaymentMethodId(), withdrawal.getUserId());
        withdrawal.setStatus(response.getStatus());
        withdrawal.setTransactionId(response.getTransactionId());
        withdrawalScheduledRepository.save(withdrawal);
        response.setExecution(WithdrawalExecution.SCHEDULED);
        response.setExecutedAt(Instant.now());
        eventsService.sendWithdrawalStatusMessage(response);
    }

    @Scheduled(fixedDelay = 5000)
    private void run() {
        withdrawalScheduledRepository.findAllByExecuteAtAfter(Instant.now())
                .forEach(this::processScheduled);
    }

    @Override
    public WithdrawalListResponse findAll() {
        List<Withdrawal> withdrawals = withdrawalRepository.findAll();
        List<WithdrawalScheduled> withdrawalsScheduled = withdrawalScheduledRepository.findAll();
        return new WithdrawalListResponse(withdrawals, withdrawalsScheduled);
    }

}
