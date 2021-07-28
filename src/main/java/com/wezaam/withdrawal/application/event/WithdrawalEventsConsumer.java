package com.wezaam.withdrawal.application.event;

import com.wezaam.withdrawal.application.command.ProcessWithdrawalCommandBuilder;
import com.wezaam.withdrawal.application.command.FinishWithdrawalProcessingCommandBuilder;
import com.wezaam.withdrawal.domain.Withdrawal;
import com.wezaam.withdrawal.domain.WithdrawalRepository;
import com.wezaam.withdrawal.domain.WithdrawalService;
import com.wezaam.withdrawal.domain.WithdrawalStatus;
import com.wezaam.withdrawal.domain.event.WithdrawalCreated;
import com.wezaam.withdrawal.domain.event.WithdrawalProcessed;
import com.wezaam.withdrawal.domain.exception.InvalidPaymentMethodException;
import com.wezaam.withdrawal.domain.exception.UserDoesNotExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class WithdrawalEventsConsumer {

    @Autowired
    private WithdrawalService withdrawalService;

    @Autowired
    private WithdrawalRepository withdrawalRepository;

    public void withdrawalCreated(WithdrawalCreated withdrawalCreated) {
        withdrawalRepository
                .findById(withdrawalCreated.getId())
                .ifPresentOrElse(
                        this::sendWithdrawalToProcess,
                        Exception::new
                );
    }

    public void withdrawalProcessed(WithdrawalProcessed withdrawalProcessed) {
        withdrawalRepository
                .findById(withdrawalProcessed.getId())
                .ifPresentOrElse(
                        this::finishWithdrawalProcessing,
                        Exception::new
                );
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    private void sendWithdrawalToProcess(Withdrawal withdrawal) {
        try {
            withdrawalService.processWithdrawal(
                    ProcessWithdrawalCommandBuilder
                            .aProcessWithdrawalCommandBuilder()
                            .withId(withdrawal.getId())
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
            withdrawalRepository.save(withdrawal);
        } catch (Exception e) {
            withdrawal.setWithdrawalStatus(WithdrawalStatus.INTERNAL_ERROR);
            withdrawalRepository.save(withdrawal);
        }
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    private void finishWithdrawalProcessing(Withdrawal withdrawal) {
        try {
            withdrawalService.finishWithdrawalProcessing(
                    FinishWithdrawalProcessingCommandBuilder
                            .aFinishWithdrawalProcessingCommandBuilder()
                            .withId(withdrawal.getId())
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
            withdrawalRepository.save(withdrawal);
        } catch (Exception e) {
            withdrawal.setWithdrawalStatus(WithdrawalStatus.INTERNAL_ERROR);
            withdrawalRepository.save(withdrawal);
        }
    }
}
