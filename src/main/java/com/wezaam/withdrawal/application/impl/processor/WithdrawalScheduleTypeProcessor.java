package com.wezaam.withdrawal.application.impl.processor;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.wezaam.withdrawal.domain.entity.PaymentMethod;
import com.wezaam.withdrawal.domain.entity.WithdrawalScheduled;
import com.wezaam.withdrawal.domain.event.WithdrawalEvent;
import com.wezaam.withdrawal.domain.exception.TransactionException;
import com.wezaam.withdrawal.domain.ports.output.external.kafka.EventsService;
import com.wezaam.withdrawal.domain.ports.output.external.payment.WithdrawalProcessingService;
import com.wezaam.withdrawal.domain.ports.output.repository.PaymentMethodRepository;
import com.wezaam.withdrawal.domain.ports.output.repository.WithdrawalScheduledRepository;
import com.wezaam.withdrawal.domain.valueobject.WithdrawalStatus;
import com.wezaam.withdrawal.domain.valueobject.WithdrawalType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class WithdrawalScheduleTypeProcessor extends AbstractWithdrawalTypeProcessor
        implements WithdrawalTypeProcessor {

    private final WithdrawalScheduledRepository withdrawalScheduledRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final WithdrawalProcessingService withdrawalProcessingService;
    private final EventsService eventsService;

    @Override
    public void execute(WithdrawalEvent withdrawalEvent) {

        withdrawalScheduledRepository
                .findById(withdrawalEvent.getId())
                .flatMap(
                        withdrawalScheduled ->
                                paymentMethodRepository
                                        .findById(withdrawalScheduled.getPaymentMethodId())
                                        .map(
                                                paymentMethod ->
                                                        processingWithdrawalScheduled(
                                                                withdrawalScheduled,
                                                                paymentMethod)))
                .orElseGet(
                        () -> {
                            log.error(
                                    "WithdrawalScheduled with id={} or payment method with id {} "
                                            + " not found",
                                    withdrawalEvent.getId(),
                                    withdrawalEvent.getPaymentMethodId());
                            return Optional.empty();
                        });
    }

    @Override
    public WithdrawalType getType() {
        return WithdrawalType.SCHEDULED;
    }

    private Optional<WithdrawalScheduled> processingWithdrawalScheduled(
            WithdrawalScheduled withdrawalScheduled, PaymentMethod paymentMethod) {

        try {
            var transactionId =
                    withdrawalProcessingService.sendToProcessing(
                            withdrawalScheduled.getAmount(), paymentMethod);
            withdrawalScheduled.setTransactionId(transactionId);
            updateStatus(withdrawalScheduled, WithdrawalStatus.PROCESSING);
        } catch (TransactionException e) {
            logError(
                    "Error processing withdrawal with id={} and payment method id ={}",
                    e,
                    withdrawalScheduled.getId(),
                    paymentMethod.getId());
            handleProcessingError(withdrawalScheduled, WithdrawalStatus.FAILED);
        } catch (Exception e) {
            logError(
                    "Internal error withdrawal with id={} and payment method id ={}",
                    e,
                    withdrawalScheduled.getId(),
                    paymentMethod.getId());
            handleProcessingError(withdrawalScheduled, WithdrawalStatus.INTERNAL_ERROR);
        }
        return Optional.of(withdrawalScheduled);
    }

    private void handleProcessingError(
            WithdrawalScheduled withdrawalScheduled, WithdrawalStatus status) {
        updateStatus(withdrawalScheduled, status);
    }

    private void updateStatus(
            WithdrawalScheduled withdrawalScheduled, WithdrawalStatus withdrawalStatus) {
        log.info(
                "Withdrawal with id={} was update with status={}",
                withdrawalScheduled.getId(),
                withdrawalStatus);

        withdrawalScheduled.setStatus(withdrawalStatus);
        withdrawalScheduledRepository.save(withdrawalScheduled);
        eventsService.send(withdrawalScheduled);
    }
}
