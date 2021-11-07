package com.wezaam.withdrawal.service;

import com.wezaam.withdrawal.repository.WithdrawalRepository;
import com.wezaam.withdrawal.repository.WithdrawalScheduledRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;

@Service
@ConditionalOnProperty("withdrawal.scheduler.enabled")
public class WithdrawalSchedulerService {

    @Autowired
    private WithdrawalRepository withdrawalRepository;
    @Autowired
    private WithdrawalScheduledRepository withdrawalScheduledRepository;
    @Autowired
    private WithdrawalService withdrawalService;
    @Autowired
    private Clock clock;

    @Scheduled(fixedDelayString = "${withdrawal.process.delay}")
    public void processPending() {
        withdrawalScheduledRepository
                .findAllToBeProcessedBefore(Instant.now(clock))
                .forEach(withdrawalService::process);
    }

    @Scheduled(fixedDelayString = "${withdrawal.notify.delay}")
    public void notifyProcessing() {
        withdrawalRepository
                .findAllToBeSent()
                .forEach(withdrawalService::sendEvent);
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }

    public void setWithdrawalRepository(WithdrawalRepository withdrawalRepository) {
        this.withdrawalRepository = withdrawalRepository;
    }

    public void setWithdrawalScheduledRepository(WithdrawalScheduledRepository withdrawalScheduledRepository) {
        this.withdrawalScheduledRepository = withdrawalScheduledRepository;
    }

    public void setWithdrawalService(WithdrawalService withdrawalService) {
        this.withdrawalService = withdrawalService;
    }
}
