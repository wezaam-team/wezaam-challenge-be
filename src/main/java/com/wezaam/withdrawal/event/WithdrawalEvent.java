package com.wezaam.withdrawal.event;

import com.wezaam.withdrawal.domain.WithdrawalMessage;
import com.wezaam.withdrawal.model.Withdrawal;
import org.springframework.context.ApplicationEvent;

public class WithdrawalEvent extends ApplicationEvent {

    private WithdrawalMessage withdrawalMessage;

    public WithdrawalEvent(Object source, WithdrawalMessage withdrawalMessage) {
        super(source);
        this.withdrawalMessage = withdrawalMessage;
    }

    public WithdrawalMessage getWithdrawalMessage() {
        return withdrawalMessage;
    }
}
