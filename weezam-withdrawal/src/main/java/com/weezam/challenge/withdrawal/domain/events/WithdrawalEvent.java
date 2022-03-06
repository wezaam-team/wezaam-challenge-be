package com.weezam.challenge.withdrawal.domain.events;

import com.weezam.challenge.withdrawal.domain.model.Withdrawal;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class WithdrawalEvent {

    private Withdrawal withdrawal;
}
