package com.wezaam.withdrawal.unit.domain;

import com.wezaam.withdrawal.domain.*;
import com.wezaam.withdrawal.domain.exception.InsufficientAmountException;
import com.wezaam.withdrawal.domain.exception.InvalidPaymentMethodException;
import com.wezaam.withdrawal.domain.exception.InvalidScheduleException;
import com.wezaam.withdrawal.domain.exception.UserDoesNotExistsException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WithdrawalShould {

    @Test
    public void notBeCreatedIfUserDoesNotHaveEnoughAmount() {
        final BigDecimal availableAmount = BigDecimal.valueOf(10);
        final BigDecimal withdrawalAmount = BigDecimal.valueOf(11);

        final User user = givenUserWithAmount(availableAmount);

        final Withdrawal withdrawal = WithdrawalBuilder.aWithdrawalBuilder()
                .withUser(user)
                .withPaymentMethod(user.getPaymentMethods().get(0))
                .withAmount(withdrawalAmount)
                .withImmediate(Boolean.TRUE)
                .build();

        assertThrows(InsufficientAmountException.class, () -> {
            withdrawal.validate();
        });
    }

    @Test
    public void notBeCreatedIfUserDoesNotExists() {
        final BigDecimal availableAmount = BigDecimal.valueOf(10);

        final Withdrawal withdrawal = WithdrawalBuilder.aWithdrawalBuilder()
                .withUser(null)
                .withPaymentMethod(getPaymentMethod(availableAmount))
                .withAmount(availableAmount)
                .withImmediate(Boolean.TRUE)
                .build();

        assertThrows(UserDoesNotExistsException.class, () -> {
            withdrawal.validate();
        });
    }

    @Test
    public void notBeCreatedIfUserDoesNotHavePaymentMethod() {
        final BigDecimal availableAmount = BigDecimal.valueOf(10);

        final User user = new User(1L, "Name", null);

        final Withdrawal withdrawal = WithdrawalBuilder.aWithdrawalBuilder()
                .withUser(user)
                .withPaymentMethod(getPaymentMethod(availableAmount))
                .withAmount(availableAmount)
                .withImmediate(Boolean.TRUE)
                .build();

        assertThrows(InvalidPaymentMethodException.class, () -> {
            withdrawal.validate();
        });
    }

    @Test
    public void notBeCreatedIfWithoutPaymentMethod() {
        final BigDecimal withdrawalAmount = BigDecimal.valueOf(11);

        final User user = givenUserWithAmount(withdrawalAmount);

        final Withdrawal withdrawal = WithdrawalBuilder.aWithdrawalBuilder()
                .withUser(user)
                .withPaymentMethod(null)
                .withAmount(withdrawalAmount)
                .withImmediate(Boolean.TRUE)
                .build();

        assertThrows(InvalidPaymentMethodException.class, () -> {
            withdrawal.validate();
        });
    }

    @Test
    public void notBeCreatedIfUserDoesNotHaveTheSamePaymentMethod() {
        final BigDecimal withdrawalAmount = BigDecimal.valueOf(11);

        final User user = givenUserWithAmount(withdrawalAmount);

        final Withdrawal withdrawal = WithdrawalBuilder.aWithdrawalBuilder()
                .withUser(user)
                .withPaymentMethod(new PaymentMethod(2L, "", null, withdrawalAmount))
                .withAmount(withdrawalAmount)
                .withImmediate(Boolean.TRUE)
                .build();

        assertThrows(InvalidPaymentMethodException.class, () -> {
            withdrawal.validate();
        });
    }

    @Test
    public void notBeCreatedIfNotImmediateNeitherScheduled() {
        final BigDecimal availableAmount = BigDecimal.valueOf(10);

        final User user = givenUserWithAmount(availableAmount);

        final Withdrawal withdrawal = WithdrawalBuilder.aWithdrawalBuilder()
                .withUser(user)
                .withPaymentMethod(user.getPaymentMethods().get(0))
                .withAmount(availableAmount)
                .build();

        assertThrows(InvalidScheduleException.class, () -> {
            withdrawal.validate();
        });
    }

    @Test
    public void notBeSentToProviderIfNotScheduleFor() {
        final BigDecimal availableAmount = BigDecimal.valueOf(10);
        final Instant tomorrow = Instant.now().plus(1, ChronoUnit.DAYS);

        final User user = givenUserWithAmount(availableAmount);
        final Withdrawal withdrawal = WithdrawalBuilder.aWithdrawalBuilder()
                .withUser(user)
                .withPaymentMethod(user.getPaymentMethods().get(0))
                .withAmount(availableAmount)
                .withScheduledFor(tomorrow)
                .build();

        assertEquals(Boolean.FALSE, withdrawal.canBeSent());
        assertEquals(WithdrawalStatus.PENDING, withdrawal.getWithdrawalStatus());
    }

    @Test
    public void notBeSentToProviderIfNotPending() {
        final BigDecimal availableAmount = BigDecimal.valueOf(10);
        final Instant tomorrow = Instant.now().plus(1, ChronoUnit.DAYS);

        final User user = givenUserWithAmount(availableAmount);
        final Withdrawal withdrawal = WithdrawalBuilder.aWithdrawalBuilder()
                .withUser(user)
                .withPaymentMethod(user.getPaymentMethods().get(0))
                .withAmount(availableAmount)
                .withScheduledFor(tomorrow)
                .withWithdrawalStatus(WithdrawalStatus.FAILED)
                .build();

        assertEquals(Boolean.FALSE, withdrawal.canBeSent());
    }

    private User givenUserWithAmount(BigDecimal availableAmount) {
        final PaymentMethod paymentMethod = getPaymentMethod(availableAmount);

        return new User(1L, "Name",
                Collections.singletonList(
                        paymentMethod
                ));
    }

    private PaymentMethod getPaymentMethod(BigDecimal availableAmount) {
        final PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setId(1L);
        paymentMethod.setName("Payment");
        paymentMethod.setAvailableAmount(availableAmount);
        return paymentMethod;
    }

}
