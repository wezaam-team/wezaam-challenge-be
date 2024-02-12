package com.wezaam.withdrawal.domain.event;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.wezaam.withdrawal.domain.WithdrawalStatus;
import com.wezaam.withdrawal.infrastructure.config.DefaultInstantDeserializer;
import com.wezaam.withdrawal.infrastructure.config.DefaultInstantSerializer;

import java.math.BigDecimal;
import java.time.Instant;

public class WithdrawalEvent {

    private Long id;

    private Long userId;

    private Long paymentMethodId;

    private BigDecimal amount;

    private Boolean immediate;

    @JsonDeserialize(using = DefaultInstantDeserializer.class)
    @JsonSerialize(using = DefaultInstantSerializer.class)
    private Instant scheduledFor;

    private WithdrawalStatus withdrawalStatus;

    public WithdrawalEvent() {
        this(null,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    protected WithdrawalEvent(Long id, Long userId, Long paymentMethodId, BigDecimal amount, Boolean immediate, Instant scheduledFor, WithdrawalStatus withdrawalStatus) {
        super();
        this.id = id;
        this.userId = userId;
        this.paymentMethodId = paymentMethodId;
        this.amount = amount;
        this.immediate = immediate;
        this.scheduledFor = scheduledFor;
        this.withdrawalStatus = withdrawalStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(Long paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Boolean getImmediate() {
        return immediate;
    }

    public void setImmediate(Boolean immediate) {
        this.immediate = immediate;
    }

    public Instant getScheduledFor() {
        return scheduledFor;
    }

    public void setScheduledFor(Instant scheduledFor) {
        this.scheduledFor = scheduledFor;
    }

    public WithdrawalStatus getWithdrawalStatus() {
        return withdrawalStatus;
    }

    public void setWithdrawalStatus(WithdrawalStatus withdrawalStatus) {
        this.withdrawalStatus = withdrawalStatus;
    }
}
