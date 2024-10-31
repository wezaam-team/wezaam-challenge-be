package com.wezaam.withdrawal.application;

import com.wezaam.withdrawal.domain.event.WithdrawalEvent;

public interface WithdrawalProcessorService {

    void process(WithdrawalEvent withdrawalMessage);
}
