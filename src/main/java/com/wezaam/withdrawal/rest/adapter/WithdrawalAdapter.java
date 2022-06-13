package com.wezaam.withdrawal.rest.adapter;

import com.wezaam.withdrawal.rest.api.withdrawal.CreateWithdrawalDto;
import com.wezaam.withdrawal.rest.api.withdrawal.WithdrawalDto;

import java.util.Collection;
import java.util.List;

public interface WithdrawalAdapter {
    Collection<WithdrawalDto> findAll();

    void create(CreateWithdrawalDto withdrawalDto);
}
