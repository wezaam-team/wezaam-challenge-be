package com.wezaam.withdrawal.service;

import com.wezaam.withdrawal.model.InstantWithdrawal;
import com.wezaam.withdrawal.model.WithdrawalScheduled;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EventsService {

    @Async
    public void send(InstantWithdrawal instantWithdrawal) {
        // build and send an event in message queue async
    }

    @Async
    public void send(WithdrawalScheduled withdrawal) {
        // build and send an event in message queue async
    }
}
