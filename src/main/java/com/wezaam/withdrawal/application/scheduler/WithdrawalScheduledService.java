package com.wezaam.withdrawal.application.scheduler;

import com.wezaam.withdrawal.application.WithdrawalInvalidatorService;
import com.wezaam.withdrawal.application.command.ProcessWithdrawalCommandFromDomainConverter;
import com.wezaam.withdrawal.domain.Withdrawal;
import com.wezaam.withdrawal.domain.WithdrawalProcessingService;
import com.wezaam.withdrawal.domain.WithdrawalRepository;
import com.wezaam.withdrawal.domain.WithdrawalStatus;
import com.wezaam.withdrawal.domain.exception.InvalidPaymentMethodException;
import com.wezaam.withdrawal.domain.exception.UserDoesNotExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class WithdrawalScheduledService {

    @Autowired
    private WithdrawalProcessingService withdrawalProcessingService;

    @Autowired
    private WithdrawalRepository withdrawalRepository;

    @Autowired
    private WithdrawalInvalidatorService withdrawalInvalidatorService;

    @Scheduled(fixedDelay = 5000)
    public void run() {
        withdrawalRepository
                .findAllByScheduledForBefore(Instant.now())
                .parallelStream()
                .forEach(this::run);
    }

    private void run(Withdrawal withdrawal) {
        try {
            withdrawalProcessingService.processWithdrawal(
                    ProcessWithdrawalCommandFromDomainConverter
                            .aProcessWithdrawalCommandConverter()
                            .apply(withdrawal)
            );
        } catch (InvalidPaymentMethodException | UserDoesNotExistsException e) {
            withdrawalInvalidatorService
                    .invalidateWithdrawalWithStatus(withdrawal, WithdrawalStatus.FAILED);
        } catch (Exception e) {
            withdrawalInvalidatorService
                    .invalidateWithdrawalWithStatus(withdrawal, WithdrawalStatus.INTERNAL_ERROR);
        }
    }

}
