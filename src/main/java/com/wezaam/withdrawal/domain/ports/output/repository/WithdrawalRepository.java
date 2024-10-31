package com.wezaam.withdrawal.domain.ports.output.repository;

import java.util.List;
import java.util.Optional;

import com.wezaam.withdrawal.domain.entity.Withdrawal;

public interface WithdrawalRepository {

    Optional<Withdrawal> findById(Long withdrawalId);

    Withdrawal save(Withdrawal withdrawal);

    List<Withdrawal> findAll();
}
