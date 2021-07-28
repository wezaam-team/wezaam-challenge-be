package com.wezaam.withdrawal.integration.domain;

import java.math.BigDecimal;

public enum MockedWithdrawal {

    AVAILABLE(
            1L,
            1L,
            BigDecimal.valueOf(1000, 2)
    ),

    UNAVAILABLE(
            2L,
            2L,
            BigDecimal.valueOf(1100, 2)
    );

    Long userId;
    Long paymentId;
    BigDecimal amount;

    MockedWithdrawal(Long userId, Long paymentId, BigDecimal amount) {
        this.userId = userId;
        this.paymentId = paymentId;
        this.amount = amount;
    }

}
