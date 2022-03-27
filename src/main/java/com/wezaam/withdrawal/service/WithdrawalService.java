package com.wezaam.withdrawal.service;

import com.wezaam.withdrawal.domain.WithdrawalListResponse;
import com.wezaam.withdrawal.model.*;


public interface WithdrawalService {


    void create(Withdrawal withdrawal);
    void schedule(WithdrawalScheduled withdrawalScheduled);
    WithdrawalListResponse findAll();
}
