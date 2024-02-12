package com.wezaam.withdrawal.acceptance.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

public class Withdrawal implements Serializable {

    public Withdrawal(Long userId, Long paymentMethodId, BigDecimal amount, Boolean immediate, Instant scheduledFor) {
        this.userId = userId;
        this.paymentMethodId = paymentMethodId;
        this.amount = amount;
        this.immediate = immediate;
        this.scheduledFor = scheduledFor;
    }

    private Long userId;

    private Long paymentMethodId;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER, pattern = "#,###.00")
    private BigDecimal amount;

    private Boolean immediate;

    private Instant scheduledFor;

}