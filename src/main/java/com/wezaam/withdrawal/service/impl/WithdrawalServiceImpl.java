package com.wezaam.withdrawal.service.impl;

import com.wezaam.withdrawal.exception.TransactionException;
import com.wezaam.withdrawal.model.*;
import com.wezaam.withdrawal.repository.PaymentMethodRepository;
import com.wezaam.withdrawal.repository.WithdrawalRepository;
import com.wezaam.withdrawal.repository.WithdrawalScheduledRepository;
import com.wezaam.withdrawal.service.WithdrawalProcessingService;
import com.wezaam.withdrawal.service.event.WithdrawalEventPublisher;
import com.wezaam.withdrawal.service.EventsService;
import com.wezaam.withdrawal.service.WithdrawalService;

import org.springframework.stereotype.Service;

@Service
public class WithdrawalServiceImpl implements WithdrawalService {

    private final WithdrawalRepository withdrawalRepository;
    private final WithdrawalScheduledRepository withdrawalScheduledRepository;
    private final WithdrawalProcessingService withdrawalProcessingService;
    private final PaymentMethodRepository paymentMethodRepository;
    private final EventsService eventsService;
    private final WithdrawalEventPublisher withdrawalEventPublisher;

    public WithdrawalServiceImpl(WithdrawalRepository withdrawalRepository,
                                 WithdrawalScheduledRepository withdrawalScheduledRepository,
                                 WithdrawalProcessingService withdrawalProcessingService,
                                 PaymentMethodRepository paymentMethodRepository,
                                 EventsService eventsService,
                                 WithdrawalEventPublisher withdrawalEventPublisher) {
        this.withdrawalRepository = withdrawalRepository;
        this.withdrawalScheduledRepository = withdrawalScheduledRepository;
        this.withdrawalProcessingService = withdrawalProcessingService;
        this.paymentMethodRepository = paymentMethodRepository;
        this.eventsService = eventsService;
        this.withdrawalEventPublisher = withdrawalEventPublisher;
    }

    @Override
    public void create(Withdrawal withdrawal) {
        Withdrawal pendingWithdrawal = withdrawalRepository.save(withdrawal);
        withdrawalEventPublisher.publishWithdrawalEvent(pendingWithdrawal);
    }

    @Override
    public void schedule(WithdrawalScheduled withdrawalScheduled) {
        withdrawalScheduledRepository.save(withdrawalScheduled);
    }

    @Override
    public void processWithdrawal(Withdrawal pendingWithdrawal) {
        withdrawalRepository.findById(pendingWithdrawal.getId())
                .ifPresent(this::process);
    }

    private void process(Withdrawal savedWithdrawal) {
        sendToProcessing(savedWithdrawal);
        withdrawalRepository.save(savedWithdrawal);
        eventsService.send(savedWithdrawal);
    }

    @Override
    public void processScheduled(WithdrawalScheduled withdrawal) {
        sendToProcessing(withdrawal);
        withdrawalScheduledRepository.save(withdrawal);
        eventsService.send(withdrawal);
    }

    private void sendToProcessing(AbstractWithdrawal withdrawal) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(withdrawal.getPaymentMethodId()).orElse(null);

        if (paymentMethod != null) {
            try {
                Long transactionId = withdrawalProcessingService.sendToProcessing(withdrawal.getAmount(), paymentMethod);
                withdrawal.setStatus(WithdrawalStatus.PROCESSING);
                withdrawal.setTransactionId(transactionId);
            } catch (TransactionException e) {
                withdrawal.setStatus(WithdrawalStatus.FAILED);
            } catch (Exception e) {
                withdrawal.setStatus(WithdrawalStatus.INTERNAL_ERROR);
            }
        }
    }
}
