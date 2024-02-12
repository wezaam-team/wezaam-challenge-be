package com.wezaam.withdrawal.integration;

import java.math.BigDecimal;

public enum MockedWithdrawal {

    AVAILABLE(
            1L,
            1L,
            BigDecimal.valueOf(1000, 2)
    ),

    UNAVAILABLE(
            100L,
            100L,
            BigDecimal.valueOf(1100, 2)
    );

    public Long userId;
    public Long paymentId;
    public BigDecimal amount;

    MockedWithdrawal(Long userId, Long paymentId, BigDecimal amount) {
        this.userId = userId;
        this.paymentId = paymentId;
        this.amount = amount;
    }

}
