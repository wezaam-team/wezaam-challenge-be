package com.wezaam.withdrawal.domain.ports.output.external.kafka;

import com.wezaam.withdrawal.domain.entity.Withdrawal;
import com.wezaam.withdrawal.domain.entity.WithdrawalScheduled;

public interface EventsService {

    void send(Withdrawal withdrawal);

    void send(WithdrawalScheduled withdrawal);
}
