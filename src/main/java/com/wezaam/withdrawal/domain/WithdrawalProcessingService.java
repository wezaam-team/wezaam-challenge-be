package com.wezaam.withdrawal.domain;

import com.wezaam.withdrawal.application.command.FinishWithdrawalProcessingCommand;
import com.wezaam.withdrawal.application.command.ProcessWithdrawalCommand;
import com.wezaam.withdrawal.application.event.EventPublisher;
import com.wezaam.withdrawal.domain.event.WithdrawalClosedConverter;
import com.wezaam.withdrawal.domain.event.WithdrawalProcessedConverter;
import com.wezaam.withdrawal.domain.exception.InvalidPaymentMethodException;
import com.wezaam.withdrawal.domain.exception.UserDoesNotExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class WithdrawalProcessingService {

    @Autowired
    private WithdrawalRepository withdrawalRepository;

    @Autowired
    private EventPublisher eventPublisher;

    @Autowired
    private Provider provider;

    @Transactional(Transactional.TxType.REQUIRED)
    public void processWithdrawal(ProcessWithdrawalCommand processWithdrawalCommand) throws
            InvalidPaymentMethodException,
            UserDoesNotExistsException {

        Withdrawal withdrawal = getWithdrawal(processWithdrawalCommand.getId());

        if (withdrawal.canBeSent()) {
            withdrawal.setWithdrawalStatus(WithdrawalStatus.PROCESSING);
            withdrawalRepository.save(withdrawal);
            provider.processWithdrawal(withdrawal);

            eventPublisher.publish(
                    WithdrawalProcessedConverter
                            .aWithdrawalProcessedConverter()
                            .apply(withdrawal)
            );
        }
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void finishWithdrawalProcessing(FinishWithdrawalProcessingCommand finishWithdrawalProcessingCommand) throws
            InvalidPaymentMethodException,
            UserDoesNotExistsException {

        Withdrawal withdrawal = getWithdrawal(finishWithdrawalProcessingCommand.getId());
        if (withdrawal.canBeClosed()) {
            withdrawal.setWithdrawalStatus(WithdrawalStatus.SUCCESS);
            withdrawalRepository.save(withdrawal);

            eventPublisher.publish(
                    WithdrawalClosedConverter
                            .aWithdrawalClosedConverter()
                            .apply(withdrawal)
            );
        }
    }

    private Withdrawal getWithdrawal(Long withdrawalId) {
        return withdrawalRepository
                .findById(withdrawalId)
                .orElseThrow();
    }
}
