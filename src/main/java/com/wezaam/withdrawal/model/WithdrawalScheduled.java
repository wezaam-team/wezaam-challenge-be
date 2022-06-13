package com.wezaam.withdrawal.model;

import javax.persistence.Entity;
import java.time.Instant;

@Entity(name = "scheduled_withdrawals")
public class WithdrawalScheduled extends AbstractWithdrawal {

    private Instant executeAt;

    public Instant getExecuteAt() {
        return executeAt;
    }

    public void setExecuteAt(Instant executeAt) {
        this.executeAt = executeAt;
    }
}
