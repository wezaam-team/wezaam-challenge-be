package com.wezaam.withdrawal.application.event;

import com.wezaam.withdrawal.application.WithdrawalInvalidatorService;
import com.wezaam.withdrawal.application.command.FinishWithdrawalProcessingCommandFromDomainConverter;
import com.wezaam.withdrawal.application.command.ProcessWithdrawalCommandFromDomainConverter;
import com.wezaam.withdrawal.domain.Withdrawal;
import com.wezaam.withdrawal.domain.WithdrawalProcessingService;
import com.wezaam.withdrawal.domain.WithdrawalRepository;
import com.wezaam.withdrawal.domain.WithdrawalStatus;
import com.wezaam.withdrawal.domain.event.*;
import com.wezaam.withdrawal.domain.exception.InvalidPaymentMethodException;
import com.wezaam.withdrawal.domain.exception.UserDoesNotExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.logging.Logger;

@Service
public class WithdrawalEventsConsumer {

    private static final Logger LOGGER =
            Logger.getLogger(WithdrawalEventsConsumer.class.getSimpleName());

    @Autowired
    private WithdrawalProcessingService withdrawalProcessingService;

    @Autowired
    private WithdrawalRepository withdrawalRepository;

    @Autowired
    private WithdrawalInvalidatorService withdrawalInvalidatorService;

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

    public void withdrawalClosed(WithdrawalClosed withdrawalClosed) {
        LOGGER.info(
                String.format(
                        "Withdrawal #%d was closed!",
                        withdrawalClosed.getId()
                )
        );
    }

    public void withdrawalInvalidated(WithdrawalInvalidated withdrawalInvalidated) {
        LOGGER.info(
                String.format(
                        "Withdrawal #%d was invalidated!",
                        withdrawalInvalidated.getId()
                )
        );
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    private void sendWithdrawalToProcess(Withdrawal withdrawal) {
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

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    private void finishWithdrawalProcessing(Withdrawal withdrawal) {
        try {
            withdrawalProcessingService.finishWithdrawalProcessing(
                    FinishWithdrawalProcessingCommandFromDomainConverter
                            .aFinishWithdrawalProcessingCommandFromDomainConverter()
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
