package com.wezaam.withdrawal.domain;

import com.wezaam.withdrawal.application.command.CreateWithdrawalCommand;
import com.wezaam.withdrawal.application.command.FinishWithdrawalProcessingCommand;
import com.wezaam.withdrawal.application.command.ProcessWithdrawalCommand;
import com.wezaam.withdrawal.application.command.WithdrawalCommand;
import com.wezaam.withdrawal.application.event.EventPublisher;
import com.wezaam.withdrawal.domain.event.WithdrawalCreatedConverter;
import com.wezaam.withdrawal.domain.event.WithdrawalProcessedConverter;
import com.wezaam.withdrawal.domain.exception.InsufficientAmountException;
import com.wezaam.withdrawal.domain.exception.InvalidPaymentMethodException;
import com.wezaam.withdrawal.domain.exception.InvalidScheduleException;
import com.wezaam.withdrawal.domain.exception.UserDoesNotExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class WithdrawalService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private WithdrawalRepository withdrawalRepository;

    @Autowired
    private EventPublisher eventPublisher;

    @Autowired
    private Provider provider;

    @Transactional(Transactional.TxType.REQUIRED)
    public Withdrawal createWithdrawal(CreateWithdrawalCommand createWithdrawalCommand)
            throws InsufficientAmountException,
            UserDoesNotExistsException,
            InvalidPaymentMethodException,
            InvalidScheduleException {

        Withdrawal withdrawal = getWithdrawal(createWithdrawalCommand);
        withdrawal.validate();
        withdrawal = withdrawalRepository.save(withdrawal);

        eventPublisher.publish(
                new WithdrawalCreatedConverter()
                        .apply(withdrawal)
        );

        return withdrawal;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void processWithdrawal(ProcessWithdrawalCommand processWithdrawalCommand) throws
            InvalidPaymentMethodException,
            UserDoesNotExistsException {

        Withdrawal withdrawal = getWithdrawal(processWithdrawalCommand);

        if (withdrawal.canBeSent()) {
            withdrawal.setWithdrawalStatus(WithdrawalStatus.PROCESSING);
            withdrawalRepository.save(withdrawal);
            provider.processWithdrawal(withdrawal);

            eventPublisher.publish(
                    new WithdrawalProcessedConverter()
                            .apply(withdrawal)
            );
        }
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void finishWithdrawalProcessing(FinishWithdrawalProcessingCommand finishWithdrawalProcessingCommand) throws
            InvalidPaymentMethodException,
            UserDoesNotExistsException {

        Withdrawal withdrawal = getWithdrawal(finishWithdrawalProcessingCommand);
        withdrawal.setWithdrawalStatus(WithdrawalStatus.SUCCESS);
        withdrawalRepository.save(withdrawal);
    }

    public Optional<Withdrawal> getWithdrawal(Long withdrawalId) {
        return withdrawalRepository.findById(withdrawalId);
    }

    private Withdrawal getWithdrawal(WithdrawalCommand withdrawalCommand) throws UserDoesNotExistsException, InvalidPaymentMethodException {
        final User withdrawalUser = getWithdrawalCreator(withdrawalCommand.getUserId());
        final PaymentMethod paymentMethod = getPaymentMethod(withdrawalCommand.getPaymentMethodId());

        Withdrawal withdrawal = WithdrawalBuilder.aWithdrawalBuilder()
                .withId(withdrawalCommand.getId())
                .withAmount(withdrawalCommand.getAmount())
                .withUser(withdrawalUser)
                .withPaymentMethod(paymentMethod)
                .withImmediate(withdrawalCommand.getImmediate())
                .withScheduledFor(withdrawalCommand.getScheduledFor())
                .build();
        return withdrawal;
    }

    private User getWithdrawalCreator(Long userId) throws UserDoesNotExistsException {
        return userRepository
                .findByIdWithPaymentMethods(userId)
                .orElseThrow(UserDoesNotExistsException::new);
    }

    private PaymentMethod getPaymentMethod(Long paymentMethodId) throws InvalidPaymentMethodException {
        return paymentMethodRepository
                .findById(paymentMethodId)
                .orElseThrow(InvalidPaymentMethodException::new);
    }

}
