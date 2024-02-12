package com.wezaam.withdrawal.application.scheduler;

import com.wezaam.withdrawal.application.WithdrawalInvalidatorService;
import com.wezaam.withdrawal.application.command.ProcessWithdrawalCommandFromDomainConverter;
import com.wezaam.withdrawal.domain.Withdrawal;
import com.wezaam.withdrawal.domain.WithdrawalProcessingService;
import com.wezaam.withdrawal.domain.WithdrawalRepository;
import com.wezaam.withdrawal.domain.WithdrawalStatus;
import com.wezaam.withdrawal.domain.exception.InvalidPaymentMethodException;
import com.wezaam.withdrawal.domain.exception.UserDoesNotExistsException;
import com.wezaam.withdrawal.infrastructure.config.SchedulerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.logging.Logger;

@Service
public class WithdrawalScheduledService {

    private static final Logger LOGGER =
            Logger.getLogger(WithdrawalScheduledService.class.getSimpleName());

    @Autowired
    private WithdrawalProcessingService withdrawalProcessingService;

    @Autowired
    private WithdrawalRepository withdrawalRepository;

    @Autowired
    private WithdrawalInvalidatorService withdrawalInvalidatorService;

    @Scheduled(fixedDelay = SchedulerConfig.DELAY_BETWEEN_EXECUTIONS)
    public void run() {
        LOGGER.info("Running scheduled execution to search for scheduled withdrawals.");

        withdrawalRepository
                .findAllByScheduledForBeforeAndWithdrawalStatus(Instant.now(), WithdrawalStatus.PENDING)
                .parallelStream()
                .forEach(this::run);
    }

    private void run(Withdrawal withdrawal) {
        try {
            LOGGER.info(
                    String.format(
                            "Withdrawal #%d reached the schedule time for its execution. Processing it!",
                            withdrawal.getId()
                    )
            );

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
