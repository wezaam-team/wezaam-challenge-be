package com.wezaam.withdrawal.application.event;

import com.wezaam.withdrawal.domain.event.WithdrawalEvent;

public interface EventPublisher {

    void publish(WithdrawalEvent event);
}
