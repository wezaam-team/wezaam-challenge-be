package com.wezaam.withdrawal.service;

import com.wezaam.withdrawal.model.Withdrawal;
import com.wezaam.withdrawal.model.WithdrawalScheduled;
import org.springframework.stereotype.Service;

@Service
public class EventsService {

    public void send(Withdrawal withdrawal) {
        // send an event in message queue
    }

    public void send(WithdrawalScheduled withdrawal) {
        // send an event in message queue
    }
}
