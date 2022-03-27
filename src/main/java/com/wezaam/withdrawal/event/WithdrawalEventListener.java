package com.wezaam.withdrawal.event;

import com.wezaam.withdrawal.model.Withdrawal;
import com.wezaam.withdrawal.model.WithdrawalStatus;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class WithdrawalEventListener implements ApplicationListener<WithdrawalEvent> {

    @Override
    public void onApplicationEvent(WithdrawalEvent withdrawalEvent) {
        System.out.println(withdrawalEvent.getWithdrawalMessage().getMessage());
    }
}
