package com.wezaam.withdrawal.service.event;

import com.wezaam.withdrawal.model.Withdrawal;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class WithdrawalEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public WithdrawalEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishWithdrawalEvent(final Withdrawal withdrawal) {
        applicationEventPublisher.publishEvent(withdrawal);
    }
}
