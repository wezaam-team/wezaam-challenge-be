package com.wezaam.withdrawal.application.impl.processor;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.wezaam.withdrawal.domain.entity.PaymentMethod;
import com.wezaam.withdrawal.domain.entity.Withdrawal;
import com.wezaam.withdrawal.domain.event.WithdrawalEvent;
import com.wezaam.withdrawal.domain.exception.TransactionException;
import com.wezaam.withdrawal.domain.ports.output.external.kafka.EventsService;
import com.wezaam.withdrawal.domain.ports.output.external.payment.WithdrawalProcessingService;
import com.wezaam.withdrawal.domain.ports.output.repository.PaymentMethodRepository;
import com.wezaam.withdrawal.domain.ports.output.repository.WithdrawalRepository;
import com.wezaam.withdrawal.domain.valueobject.WithdrawalStatus;
import com.wezaam.withdrawal.domain.valueobject.WithdrawalType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class WithdrawalAsapTypeProcessor extends AbstractWithdrawalTypeProcessor
        implements WithdrawalTypeProcessor {

    private final WithdrawalRepository withdrawalRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final WithdrawalProcessingService withdrawalProcessingService;
    private final EventsService eventsService;

    @Override
    public void execute(WithdrawalEvent withdrawalEvent) {

        log.info(
                "Starting process withdrawal type {} with id={} and payment method id={}",
                withdrawalEvent.getWithdrawalType(),
                withdrawalEvent.getId(),
                withdrawalEvent.getPaymentMethodId());

        withdrawalRepository
                .findById(withdrawalEvent.getId())
                .flatMap(
                        withdrawal ->
                                paymentMethodRepository
                                        .findById(withdrawal.getPaymentMethodId())
                                        .map(
                                                paymentMethod ->
                                                        processingWithdrawal(
                                                                withdrawal, paymentMethod)))
                .orElseGet(
                        () -> {
                            log.error(
                                    "Withdrawal with id={} or payment method with id {}  not found",
                                    withdrawalEvent.getId(),
                                    withdrawalEvent.getPaymentMethodId());
                            return Optional.empty();
                        });
    }

    @Override
    public WithdrawalType getType() {
        return WithdrawalType.ASAP;
    }

    private Optional<Withdrawal> processingWithdrawal(
            Withdrawal withdrawal, PaymentMethod paymentMethod) {

        try {
            var transactionId =
                    withdrawalProcessingService.sendToProcessing(
                            withdrawal.getAmount(), paymentMethod);
            withdrawal.setTransactionId(transactionId);
            updateStatus(withdrawal, WithdrawalStatus.PROCESSING);
        } catch (TransactionException e) {
            logError(
                    "Error processing withdrawal with id={} and payment method id ={}",
                    e,
                    withdrawal.getId(),
                    paymentMethod.getId());
            handleProcessingError(withdrawal, WithdrawalStatus.FAILED);
        } catch (Exception e) {
            logError(
                    "Internal error withdrawal with id={} and payment method id ={}",
                    e,
                    withdrawal.getId(),
                    paymentMethod.getId());
            handleProcessingError(withdrawal, WithdrawalStatus.INTERNAL_ERROR);
        }
        return Optional.of(withdrawal);
    }

    private void handleProcessingError(Withdrawal withdrawal, WithdrawalStatus status) {
        updateStatus(withdrawal, status);
    }

    private void updateStatus(Withdrawal withdrawal, WithdrawalStatus withdrawalStatus) {
        log.info(
                "Withdrawal with id={} was update with status={}",
                withdrawal.getId(),
                withdrawalStatus);
        withdrawal.setStatus(withdrawalStatus);
        withdrawalRepository.save(withdrawal);
        eventsService.send(withdrawal);
    }
}
