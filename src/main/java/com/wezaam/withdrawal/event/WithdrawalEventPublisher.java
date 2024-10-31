package com.wezaam.withdrawal.event;

import com.wezaam.withdrawal.domain.WithdrawalMessage;
import com.wezaam.withdrawal.model.Withdrawal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class WithdrawalEventPublisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishEvent(final WithdrawalMessage message) {
        WithdrawalEvent withdrawalEvent = new WithdrawalEvent(this, message);
        applicationEventPublisher.publishEvent(withdrawalEvent);
    }
}
