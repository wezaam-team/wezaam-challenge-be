package com.wezaam.withdrawal.domain;

public interface Provider {

    void processWithdrawal(Withdrawal withdrawal);
}
