package com.wezaam.withdrawal.model;

import com.wezaam.withdrawal.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import java.time.Instant;


@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "scheduled_withdrawals")
public class WithdrawalScheduled extends Withdrawal {
    @Getter
    @Setter
    private Instant executeAt;

    //Alternative setter, used by ModelMapper
    public void setExecuteAt(String executeAt) {
        this.executeAt = Utils.parseStringTimeToInstant(executeAt);
    }
}
