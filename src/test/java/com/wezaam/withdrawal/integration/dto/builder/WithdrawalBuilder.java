package com.wezaam.withdrawal.integration.dto.builder;

import com.wezaam.withdrawal.integration.dto.User;
import com.wezaam.withdrawal.integration.dto.Withdrawal;

import java.math.BigDecimal;

public class WithdrawalBuilder {
    private User user;
    private BigDecimal ammount;

    public static WithdrawalBuilder aWithdrawalBuilder() {
        return new WithdrawalBuilder();
    }

    public WithdrawalBuilder withUser(User user) {
        this.user = user;
        return this;
    }

    public WithdrawalBuilder withAmmount(BigDecimal ammount) {
        this.ammount = ammount;
        return this;
    }

    public Withdrawal build() {
        return new Withdrawal(user, ammount);
    }
}