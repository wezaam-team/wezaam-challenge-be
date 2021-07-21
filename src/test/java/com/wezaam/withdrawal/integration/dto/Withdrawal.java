package com.wezaam.withdrawal.integration.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class Withdrawal implements Serializable {
    public Withdrawal(User user, BigDecimal ammount) {
        this.user = user;
        this.ammount = ammount;
    }

    private User user;
    private BigDecimal ammount;
}
