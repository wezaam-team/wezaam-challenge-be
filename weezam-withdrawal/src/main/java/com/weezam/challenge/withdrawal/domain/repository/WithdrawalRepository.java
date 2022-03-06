package com.weezam.challenge.withdrawal.domain.repository;

import com.weezam.challenge.withdrawal.domain.model.Withdrawal;

import java.util.List;
import java.util.Optional;

public interface WithdrawalRepository {

    List<Withdrawal> findAll();

    Optional<Withdrawal> findOne(final Long id);

    Withdrawal save(final Withdrawal withdrawal);
}
