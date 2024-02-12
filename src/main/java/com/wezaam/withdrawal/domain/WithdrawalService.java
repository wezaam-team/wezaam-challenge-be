package com.wezaam.withdrawal.domain;

import com.wezaam.withdrawal.application.command.CreateWithdrawalCommand;
import com.wezaam.withdrawal.application.event.EventPublisher;
import com.wezaam.withdrawal.domain.converter.WithdrawalFromCommandConverter;
import com.wezaam.withdrawal.domain.event.WithdrawalCreatedConverter;
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
                WithdrawalCreatedConverter
                        .aWithdrawalCreatedConverter()
                        .apply(withdrawal)
        );

        return withdrawal;
    }

    public Optional<Withdrawal> getWithdrawal(Long withdrawalId) {
        return withdrawalRepository.findById(withdrawalId);
    }

    private Withdrawal getWithdrawal(CreateWithdrawalCommand withdrawalCommand) throws UserDoesNotExistsException, InvalidPaymentMethodException {
        final Withdrawal withdrawal = WithdrawalFromCommandConverter
                .aWithdrawalFromCommandConverter()
                .apply(withdrawalCommand);

        withdrawal.setUser(
                getWithdrawalCreator(withdrawalCommand.getUserId()));
        withdrawal.setPaymentMethod(
                getPaymentMethod(withdrawalCommand.getPaymentMethodId()));

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
