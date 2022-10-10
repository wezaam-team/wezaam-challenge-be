package com.wezaam.withdrawal.port.adapter.scheduling;

import com.wezaam.withdrawal.application.WithdrawalApplicationService;
import com.wezaam.withdrawal.domain.model.Withdrawal;
import com.wezaam.withdrawal.domain.model.WithdrawalRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledWithdrawalProcessingTriggerer {

    private final WithdrawalRepository withdrawalRepository;
    private final WithdrawalApplicationService withdrawalApplicationService;

    public ScheduledWithdrawalProcessingTriggerer(
            WithdrawalRepository withdrawalRepository, WithdrawalApplicationService withdrawalApplicationService) {

        if (withdrawalRepository == null) {
            throw new IllegalArgumentException("The withdrawalRepository should not be null.");
        }

        if (withdrawalApplicationService == null) {
            throw new IllegalArgumentException("The withdrawalApplicationService should not be null.");
        }

        this.withdrawalRepository = withdrawalRepository;
        this.withdrawalApplicationService = withdrawalApplicationService;
    }

    @Scheduled(fixedDelay = 5_000)
    public void processReadyScheduledWithdrawals() {
        this.withdrawalRepository.getScheduledWithdrawalsReadyForProcessing()
                .forEach(this::processReadyScheduledWithdrawal);
    }

    private void processReadyScheduledWithdrawal(Withdrawal withdrawal) {
        this.withdrawalApplicationService.triggerWithdrawalProcessing(withdrawal.getId());
    }
}
