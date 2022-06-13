package com.wezaam.withdrawal.rest.mapper;

import com.wezaam.withdrawal.model.Withdrawal;
import com.wezaam.withdrawal.model.WithdrawalScheduled;
import com.wezaam.withdrawal.model.WithdrawalStatus;
import com.wezaam.withdrawal.rest.api.withdrawal.CreateWithdrawalDto;
import com.wezaam.withdrawal.rest.api.withdrawal.ScheduledWithdrawalDto;
import com.wezaam.withdrawal.rest.api.withdrawal.WithdrawalDto;

import java.util.function.Function;

public class WithdrawalMapper {

 public static final Function<Withdrawal, WithdrawalDto> toDto = w ->
         new WithdrawalDto(w.getId(),
                 w.getTransactionId(),
                 w.getAmount(),
                 w.getCreatedAt(),
                 w.getUserId(),
                 w.getPaymentMethodId(),
                 w.getStatus()
         );

    public static final Function<WithdrawalScheduled, ScheduledWithdrawalDto> toScheduledWithdrawalDto = w ->
            new ScheduledWithdrawalDto(w.getId(),
                    w.getTransactionId(),
                    w.getAmount(),
                    w.getCreatedAt(),
                    w.getUserId(),
                    w.getPaymentMethodId(),
                    w.getStatus(),
                    w.getExecuteAt()
            );

    public static final Function<CreateWithdrawalDto, Withdrawal> toWithdrawal = w ->{
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setUserId(w.getUserId());
        withdrawal.setPaymentMethodId(w.getPaymentMethodId());
        withdrawal.setAmount(w.getAmount());
        withdrawal.setStatus(WithdrawalStatus.PENDING);

        return withdrawal;
    };

    public static final Function<CreateWithdrawalDto, WithdrawalScheduled> toWithdrawalScheduled = w ->{
        WithdrawalScheduled withdrawalScheduled = new WithdrawalScheduled();
        withdrawalScheduled.setUserId(w.getUserId());
        withdrawalScheduled.setPaymentMethodId(w.getPaymentMethodId());
        withdrawalScheduled.setAmount(w.getAmount());
        withdrawalScheduled.setExecuteAt(w.getExecutedAt());
        withdrawalScheduled.setStatus(WithdrawalStatus.PENDING);

        return withdrawalScheduled;
    };
}
