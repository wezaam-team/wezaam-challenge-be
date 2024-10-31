package com.wezaam.withdrawal.application.impl.processor;

import com.wezaam.withdrawal.domain.event.WithdrawalEvent;
import com.wezaam.withdrawal.domain.valueobject.WithdrawalType;

public interface WithdrawalTypeProcessor {

    void execute(WithdrawalEvent withdrawalMessage);

    WithdrawalType getType();
}
