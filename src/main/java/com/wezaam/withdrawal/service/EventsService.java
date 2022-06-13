package com.wezaam.withdrawal.service;

import com.wezaam.withdrawal.model.AbstractWithdrawal;
import com.wezaam.withdrawal.model.Withdrawal;
import com.wezaam.withdrawal.model.WithdrawalScheduled;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;

public interface EventsService {

    @Async
    @Retryable(value = RuntimeException.class, backoff = @Backoff(value = 100L))
    void send(Withdrawal withdrawal);

    @Async
    void send(WithdrawalScheduled withdrawal);

    @Recover
    void recover(RuntimeException e, AbstractWithdrawal withdrawal);

}
