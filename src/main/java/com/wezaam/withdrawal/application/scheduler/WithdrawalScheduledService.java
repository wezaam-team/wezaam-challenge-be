package com.wezaam.withdrawal.application.scheduler;

import com.wezaam.withdrawal.application.command.ProcessWithdrawalCommandBuilder;
import com.wezaam.withdrawal.domain.Withdrawal;
import com.wezaam.withdrawal.domain.WithdrawalRepository;
import com.wezaam.withdrawal.domain.WithdrawalService;
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
    private WithdrawalService withdrawalService;

    @Autowired
    private WithdrawalRepository withdrawalRepository;

    @Scheduled(fixedDelay = 5000)
    public void run() {
        withdrawalRepository
                .findAllByScheduledForBefore(Instant.now())
                .forEach(this::run);
    }

    private void run(Withdrawal withdrawal) {
        try {
            withdrawalService.processWithdrawal(
                    ProcessWithdrawalCommandBuilder
                            .aProcessWithdrawalCommandBuilder()
                            .withScheduledFor(withdrawal.getScheduledFor())
                            .withAmount(withdrawal.getAmount())
                            .withImmediate(withdrawal.getImmediate())
                            .withWithdrawalStatus(withdrawal.getWithdrawalStatus())
                            .withUserId(withdrawal.getUser().getId())
                            .withPaymentMethodId(withdrawal.getPaymentMethod().getId())
                            .build()
            );
        } catch (InvalidPaymentMethodException | UserDoesNotExistsException e) {
            withdrawal.setWithdrawalStatus(WithdrawalStatus.FAILED);
        } catch (Exception e) {
            withdrawal.setWithdrawalStatus(WithdrawalStatus.INTERNAL_ERROR);
        } finally {
            withdrawalRepository.save(withdrawal);
        }
    }
}
