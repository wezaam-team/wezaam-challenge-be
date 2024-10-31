package com.wezaam.withdrawal.application;

import java.util.List;

import com.wezaam.withdrawal.domain.command.CreateWithdrawalCommand;
import com.wezaam.withdrawal.domain.entity.Withdrawal;

public interface WithdrawalService {

    Withdrawal create(CreateWithdrawalCommand createWithdrawalCommand);

    List<Withdrawal> getAllWithdrawal();
}
