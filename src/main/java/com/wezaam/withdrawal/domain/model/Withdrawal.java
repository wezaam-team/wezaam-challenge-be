package com.wezaam.withdrawal.domain.model;

import com.wezaam.common.domain.model.DomainEventPublisher;
import com.wezaam.withdrawal.domain.service.WithdrawalProcessingService;
import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity(name = "withdrawals")
public class Withdrawal {

    @Id
    private long id;

    private long userId;
    private long paymentMethodId;
    private double amount;
    private Instant createdAt;
    private Instant executeAt;
    private Long transactionId;

    @Enumerated(EnumType.STRING)
    private WithdrawalStatus status;

    protected Withdrawal() {}

    Withdrawal(long id, long userId, long paymentMethodId, double amount, Instant executeAt) {
        this.setId(id);
        this.setUserId(userId);
        this.setPaymentMethodId(paymentMethodId);
        this.setAmount(amount);
        this.setCreatedAt(Instant.now());
        this.setExecuteAt(executeAt);
        this.setStatus(WithdrawalStatus.PENDING);

        DomainEventPublisher.instance().publish(new WithdrawalCreated(
                this.getId(),
                this.getUserId(),
                this.getPaymentMethodId(),
                this.getAmount(),
                this.getCreatedAt(),
                this.getExecuteAt(),
                this.getStatus()));
    }

    public boolean isImmediate() {
        return this.getExecuteAt() == null;
    }

    public void triggerProcessing(WithdrawalProcessingService processingService, UserRepository userRepository) {
        if (!this.canBeProcessed()) {
            throw new IllegalStateException("Withdrawal of ID: " + this.getId() + " can not be processed.");
        }

        var user = userRepository.getUserOfId(this.getUserId())
                .orElseThrow(() -> new IllegalStateException("User of ID: " + this.getUserId() + " does not exist."));

        if (!user.hasPaymentMethodOfId(this.getPaymentMethodId())) {
            throw new IllegalStateException("User of ID: " + this.getUserId() + " does not have payment method of ID: "
                    + this.getPaymentMethodId() + ".");
        }

        var paymentMethod = user.getPaymentMethodOfId(this.getPaymentMethodId());

        try {
            var processingTransactionId = processingService.sendToProcessing(this.getAmount(), paymentMethod);
            this.setTransactionId(processingTransactionId);
            this.setStatus(WithdrawalStatus.PROCESSING);

            DomainEventPublisher.instance().publish(
                    new WithdrawalProcessingTriggered(this.getId(), this.getTransactionId(), this.getStatus()));

        } catch (Exception e) {
            WithdrawalStatus withdrawalStatus;

            if (e instanceof TransactionException) {
                withdrawalStatus = WithdrawalStatus.FAILED;
            } else {
                withdrawalStatus = WithdrawalStatus.INTERNAL_ERROR;
            }

            this.setStatus(withdrawalStatus);

            DomainEventPublisher.instance()
                    .publish(new WithdrawalProcessingTriggeringFailed(this.getId(), e.getMessage(), this.getStatus()));
        }
    }

    public boolean canBeProcessed() {
        return this.getStatus() == WithdrawalStatus.PENDING
                && (this.getExecuteAt() == null || !this.getExecuteAt().isAfter(Instant.now()));
    }

    public void handleProcessingSuccess() {
        if (this.getStatus() != WithdrawalStatus.PROCESSING && this.getStatus() != WithdrawalStatus.SUCCESS) {
            throw new IllegalStateException(
                    "Can not transition withdrawal of ID: " + this.getId() + " to status " + WithdrawalStatus.SUCCESS + "."
            );
        }

        this.setStatus(WithdrawalStatus.SUCCESS);
    }

    public void handleProcessingFailure() {
        if (this.getStatus() != WithdrawalStatus.PROCESSING && this.getStatus() != WithdrawalStatus.FAILED) {
            throw new IllegalStateException(
                    "Can not transition withdrawal of ID: " + this.getId() + " to status " + WithdrawalStatus.FAILED + "."
            );
        }

        this.setStatus(WithdrawalStatus.FAILED);
    }

    private void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

    private void setUserId(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return this.userId;
    }

    private void setPaymentMethodId(long paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public long getPaymentMethodId() {
        return this.paymentMethodId;
    }

    private void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return this.amount;
    }

    private void setExecuteAt(Instant executeAt) {
        this.executeAt = executeAt;
    }

    public Instant getExecuteAt() {
        return this.executeAt;
    }

    private void setCreatedAt(Instant createdAt) {
        if (createdAt == null) {
            throw new IllegalArgumentException("The createdAt should not be null.");
        }

        this.createdAt = createdAt;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    private void setStatus(WithdrawalStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("The status should not be null.");
        }

        this.status = status;
    }

    public WithdrawalStatus getStatus() {
        return this.status;
    }

    private void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getTransactionId() {
        return this.transactionId;
    }
}
