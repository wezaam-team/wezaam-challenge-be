package com.wezaam.withdrawal.service;

import com.wezaam.withdrawal.model.Withdrawal;
import com.wezaam.withdrawal.model.WithdrawalScheduled;

public interface WithdrawalService {

    void create(Withdrawal withdrawal);

    void schedule(WithdrawalScheduled withdrawalScheduled);

    void processWithdrawal(Withdrawal pendingWithdrawal);

    void processScheduled(WithdrawalScheduled withdrawal);

}
