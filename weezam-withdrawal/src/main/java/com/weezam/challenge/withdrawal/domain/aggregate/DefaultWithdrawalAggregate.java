package com.weezam.challenge.withdrawal.domain.aggregate;

import com.weezam.challenge.withdrawal.domain.WithdrawalAggregate;
import com.weezam.challenge.withdrawal.domain.clients.PaymentMethodClient;
import com.weezam.challenge.withdrawal.domain.clients.UserClient;
import com.weezam.challenge.withdrawal.domain.events.WithdrawalEvent;
import com.weezam.challenge.withdrawal.domain.events.WithdrawalEventPublisher;
import com.weezam.challenge.withdrawal.domain.exception.BadWithdrawalException;
import com.weezam.challenge.withdrawal.domain.exception.InvalidCriteriaException;
import com.weezam.challenge.withdrawal.domain.exception.WithdrawalApplicationException;
import com.weezam.challenge.withdrawal.domain.exception.WithdrawalNotFoundException;
import com.weezam.challenge.withdrawal.domain.model.PaymentMethod;
import com.weezam.challenge.withdrawal.domain.model.User;
import com.weezam.challenge.withdrawal.domain.model.Withdrawal;
import com.weezam.challenge.withdrawal.domain.model.WithdrawalStatus;
import com.weezam.challenge.withdrawal.domain.repository.WithdrawalRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Slf4j
public class DefaultWithdrawalAggregate implements WithdrawalAggregate {

    private final WithdrawalRepository withdrawalRepository;
    private final WithdrawalEventPublisher withdrawalEventPublisher;
    private final UserClient userClient;
    private final PaymentMethodClient paymentMethodClient;

    @Override
    public Withdrawal findOne(Long id) throws WithdrawalApplicationException {
        if (Objects.isNull(id)) {
            throw new InvalidCriteriaException(String.format("Withdrawal not found for criteria: '%d'", id));
        }
        return withdrawalRepository.findOne(id).orElseThrow(() -> new WithdrawalNotFoundException(String.format("User not found for criteria: '%d'", id)));
    }

    @Override
    public List<Withdrawal> findAll() {
        return withdrawalRepository.findAll();
    }

    @Override
    public Withdrawal create(final Withdrawal withdrawal) throws WithdrawalApplicationException{
        try {
            withdrawal.setCreatedAt(Instant.now());
            withdrawal.setStatus(WithdrawalStatus.PENDING);
            Withdrawal res = withdrawalRepository.save(withdrawal);
            withdrawalEventPublisher.publishCreatedEvent(new WithdrawalEvent(res));
            return res;
        } catch (Exception exception) {
            log.error("Exception creating withdrawal {}", withdrawal, exception);
            throw new BadWithdrawalException(exception.getMessage());
        }
    }

    @Override
    public void process(final Long id) throws WithdrawalApplicationException {
        Withdrawal withdrawal = findOne(id);
        try {
            PaymentMethod paymentMethod = paymentMethodClient.findOne(withdrawal.getPaymentMethodId()).orElseThrow(() -> new BadWithdrawalException(String.format("PaymentMethod %d not found", withdrawal.getPaymentMethodId())));
            User user = userClient.findOne(withdrawal.getUserId()).orElseThrow(() -> new BadWithdrawalException(String.format("User %d not found", withdrawal.getUserId())));
            log.info("CALLING TO PAYMENT PROVIDER: WITHDRAWAL_ID: {}, USER_ID: {}, PAYMENTMETHOD_ID: {}", withdrawal.getId(), user.getId(), paymentMethod.getId());
            withdrawal.setStatus(WithdrawalStatus.PROCESSING);
            withdrawal.setTransactionId(System.nanoTime());
            withdrawalRepository.save(withdrawal);
            withdrawalEventPublisher.publishNotificationEvent(new WithdrawalEvent(withdrawal));
        }  catch (BadWithdrawalException ex) {
            log.error("Exception processing withdrawal {}", withdrawal, ex);
            withdrawal.setStatus(WithdrawalStatus.FAILED);
            withdrawalRepository.save(withdrawal);
            withdrawalEventPublisher.publishNotificationEvent(new WithdrawalEvent(withdrawal));
        } catch (Exception ex) {
            log.error("Exception processing withdrawal {}", withdrawal, ex);
            withdrawal.setStatus(WithdrawalStatus.INTERNAL_ERROR);
            withdrawalRepository.save(withdrawal);
            withdrawalEventPublisher.publishNotificationEvent(new WithdrawalEvent(withdrawal));
        }
    }
}
