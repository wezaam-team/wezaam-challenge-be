package com.wezaam.withdrawal.service.task;

import com.wezaam.withdrawal.repository.WithdrawalScheduledRepository;
import com.wezaam.withdrawal.service.WithdrawalService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class WithdrawalScheduledTask {

    private final WithdrawalScheduledRepository withdrawalScheduledRepository;
    private final WithdrawalService withdrawalService;

    public WithdrawalScheduledTask(WithdrawalScheduledRepository withdrawalScheduledRepository,
                                   WithdrawalService withdrawalService) {
        this.withdrawalScheduledRepository = withdrawalScheduledRepository;
        this.withdrawalService = withdrawalService;
    }

    @Scheduled(fixedDelay = 5000)
    public void run() {
        withdrawalScheduledRepository.findAllByExecuteAtBefore(Instant.now())
                .forEach(withdrawalService::processScheduled);
    }
}
