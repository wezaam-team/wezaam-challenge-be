package com.wezaam.withdrawal.rest.adapter;

import com.wezaam.withdrawal.repository.WithdrawalRepository;
import com.wezaam.withdrawal.repository.WithdrawalScheduledRepository;
import com.wezaam.withdrawal.rest.api.withdrawal.CreateWithdrawalDto;
import com.wezaam.withdrawal.rest.api.withdrawal.ScheduledWithdrawalDto;
import com.wezaam.withdrawal.rest.api.withdrawal.WithdrawalDto;
import com.wezaam.withdrawal.rest.mapper.WithdrawalMapper;
import com.wezaam.withdrawal.service.WithdrawalService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class WithdrawalAdapterImpl implements WithdrawalAdapter {

    private final WithdrawalService withdrawalService;
    private final WithdrawalRepository withdrawalRepository;
    private final WithdrawalScheduledRepository withdrawalScheduledRepository;

    public WithdrawalAdapterImpl(WithdrawalService withdrawalService,
                                 WithdrawalRepository withdrawalRepository,
                                 WithdrawalScheduledRepository withdrawalScheduledRepository) {
        this.withdrawalService = withdrawalService;
        this.withdrawalRepository = withdrawalRepository;
        this.withdrawalScheduledRepository = withdrawalScheduledRepository;
    }


    @Override
    public Collection<WithdrawalDto> findAll() {
        List<WithdrawalDto> withdrawals = withdrawalRepository.findAll().stream().map(WithdrawalMapper.toDto).toList();
        List<ScheduledWithdrawalDto> withdrawalsScheduled = withdrawalScheduledRepository.findAll().stream().map(WithdrawalMapper.toScheduledWithdrawalDto).toList();;
        List<WithdrawalDto> result = new ArrayList<>();
        result.addAll(withdrawals);
        result.addAll(withdrawalsScheduled);

        return result;
    }

    @Override
    public void create(CreateWithdrawalDto withdrawalDto) {
        if ("ASAP".equalsIgnoreCase(withdrawalDto.getExecutionType())) {
            withdrawalService.create(WithdrawalMapper.toWithdrawal.apply(withdrawalDto));
        } else {
            withdrawalService.schedule(WithdrawalMapper.toWithdrawalScheduled.apply(withdrawalDto));
        }
    }
}
