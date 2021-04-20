package com.wezaam.withdrawal.service;

import com.wezaam.withdrawal.model.Withdrawal;
import com.wezaam.withdrawal.model.WithdrawalScheduled;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void send(Withdrawal withdrawal) {
        // send an email using a provider
    }

    public void send(WithdrawalScheduled withdrawal) {
        // send an email using a provider
    }
}
