package com.wezaam.withdrawal.service.event;

import com.wezaam.withdrawal.model.Withdrawal;
import com.wezaam.withdrawal.service.impl.WithdrawalServiceImpl;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class WithdrawalEventListener {

    private final WithdrawalServiceImpl withdrawalServiceImpl;

    public WithdrawalEventListener(WithdrawalServiceImpl withdrawalServiceImpl) {
        this.withdrawalServiceImpl = withdrawalServiceImpl;
    }

    @Async
    @EventListener
    public void handleContextStart(Withdrawal withdrawal) {
        withdrawalServiceImpl.processWithdrawal(withdrawal);
    }
}
