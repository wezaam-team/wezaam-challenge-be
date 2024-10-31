package com.wezaam.withdrawal.infrastructure.dataaccess.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.wezaam.withdrawal.domain.entity.Withdrawal;
import com.wezaam.withdrawal.domain.ports.output.repository.WithdrawalRepository;
import com.wezaam.withdrawal.infrastructure.dataaccess.mapper.WithdrawalDataAccessMapper;
import com.wezaam.withdrawal.infrastructure.dataaccess.repository.WithdrawalJpaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class WithdrawalRepositoryImpl implements WithdrawalRepository {

    private final WithdrawalJpaRepository withdrawalJpaRepository;
    private final WithdrawalDataAccessMapper withdrawalDataAccessMapper;

    @Override
    public Optional<Withdrawal> findById(Long withdrawalId) {
        return withdrawalJpaRepository
                .findById(withdrawalId)
                .map(withdrawalDataAccessMapper::mapWithdrawalAsWithdrawalResponse);
    }

    @Override
    public Withdrawal save(Withdrawal withdrawal) {
        com.wezaam.withdrawal.infrastructure.dataaccess.entity.Withdrawal toSave =
                withdrawalDataAccessMapper.mapWithdrawalEntity(withdrawal);
        com.wezaam.withdrawal.infrastructure.dataaccess.entity.Withdrawal saved =
                withdrawalJpaRepository.save(toSave);
        return withdrawalDataAccessMapper.mapWithdrawalAsWithdrawalResponse(saved);
    }

    @Override
    public List<Withdrawal> findAll() {
        List<com.wezaam.withdrawal.infrastructure.dataaccess.entity.Withdrawal> list =
                withdrawalJpaRepository.findAll();
        return withdrawalDataAccessMapper.mapWithdrawalListAsWithdrawalResponseList(list);
    }
}
