package com.wezaam.withdrawal.infrastructure.dataaccess.adapter;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.wezaam.withdrawal.domain.entity.WithdrawalScheduled;
import com.wezaam.withdrawal.domain.ports.output.repository.WithdrawalScheduledRepository;
import com.wezaam.withdrawal.domain.valueobject.WithdrawalStatus;
import com.wezaam.withdrawal.infrastructure.dataaccess.mapper.WithdrawalScheduledDataAccessMapper;
import com.wezaam.withdrawal.infrastructure.dataaccess.repository.WithdrawalScheduledJpaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class WithdrawalScheduledRepositoryImpl implements WithdrawalScheduledRepository {

    private final WithdrawalScheduledJpaRepository withdrawalScheduledJpaRepository;
    private final WithdrawalScheduledDataAccessMapper withdrawalScheduledDataAccessMapper;

    @Override
    public Optional<WithdrawalScheduled> findById(Long withdrawalScheduledId) {
        return withdrawalScheduledJpaRepository
                .findById(withdrawalScheduledId)
                .map(withdrawalScheduledDataAccessMapper::mapAsWithdrawalDomain);
    }

    @Override
    public WithdrawalScheduled save(WithdrawalScheduled withdrawalScheduled) {
        com.wezaam.withdrawal.infrastructure.dataaccess.entity.WithdrawalScheduled toSaved =
                withdrawalScheduledDataAccessMapper.mapWithdrawalEntity(withdrawalScheduled);
        com.wezaam.withdrawal.infrastructure.dataaccess.entity.WithdrawalScheduled saved =
                withdrawalScheduledJpaRepository.save(toSaved);
        return withdrawalScheduledDataAccessMapper.mapAsWithdrawalDomain(saved);
    }

    @Override
    public List<WithdrawalScheduled> findAll() {
        List<com.wezaam.withdrawal.infrastructure.dataaccess.entity.WithdrawalScheduled> list =
                withdrawalScheduledJpaRepository.findAll();
        return withdrawalScheduledDataAccessMapper.mapAsWithdrawalScheduledDomainList(list);
    }

    @Override
    public List<WithdrawalScheduled> findAllByExecuteAtBefore(Instant instant) {
        List<com.wezaam.withdrawal.infrastructure.dataaccess.entity.WithdrawalScheduled>
                withdrawalScheduledList =
                        withdrawalScheduledJpaRepository.findAllByExecuteAtBefore(instant);
        return withdrawalScheduledDataAccessMapper.mapAsWithdrawalScheduledDomainList(
                withdrawalScheduledList);
    }

    @Override
    public List<WithdrawalScheduled> findAllByExecuteAtBeforeWithStatus(
            Instant instant, WithdrawalStatus status) {
        List<com.wezaam.withdrawal.infrastructure.dataaccess.entity.WithdrawalScheduled>
                withdrawalScheduledList =
                        withdrawalScheduledJpaRepository.findAllByExecuteAtBeforeAndStatus(
                                instant, status);
        return withdrawalScheduledDataAccessMapper.mapAsWithdrawalScheduledDomainList(
                withdrawalScheduledList);
    }
}
