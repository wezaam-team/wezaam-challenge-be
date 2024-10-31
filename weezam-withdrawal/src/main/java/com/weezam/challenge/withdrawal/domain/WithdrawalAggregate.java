package com.weezam.challenge.withdrawal.domain;


import com.weezam.challenge.withdrawal.domain.exception.WithdrawalApplicationException;
import com.weezam.challenge.withdrawal.domain.model.Withdrawal;

import java.util.List;

public interface WithdrawalAggregate {

    Withdrawal findOne(final Long id) throws WithdrawalApplicationException;

    List<Withdrawal> findAll();

    Withdrawal create(final Withdrawal withdrawal) throws WithdrawalApplicationException;

    void process(final Long id) throws WithdrawalApplicationException;

}
