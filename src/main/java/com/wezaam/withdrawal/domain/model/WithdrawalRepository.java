package com.wezaam.withdrawal.domain.model;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithdrawalRepository {

    Long getNextId();

    void save(Withdrawal withdrawal);

    Optional<Withdrawal> getWithdrawalOfId(long id);

    List<Withdrawal> getAll();

    List<Withdrawal> getScheduledWithdrawalsReadyForProcessing();
}
