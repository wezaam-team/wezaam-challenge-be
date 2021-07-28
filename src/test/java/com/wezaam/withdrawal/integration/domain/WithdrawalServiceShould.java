package com.wezaam.withdrawal.integration.domain;

import com.wezaam.withdrawal.Application;
import com.wezaam.withdrawal.application.command.CreateWithdrawalCommand;
import com.wezaam.withdrawal.application.command.CreateWithdrawalCommandBuilder;
import com.wezaam.withdrawal.config.infrastructure.H2JpaConfig;
import com.wezaam.withdrawal.domain.Withdrawal;
import com.wezaam.withdrawal.domain.WithdrawalRepository;
import com.wezaam.withdrawal.domain.WithdrawalService;
import com.wezaam.withdrawal.domain.WithdrawalStatus;
import com.wezaam.withdrawal.domain.exception.InsufficientAmountException;
import com.wezaam.withdrawal.domain.exception.InvalidPaymentMethodException;
import com.wezaam.withdrawal.domain.exception.UserDoesNotExistsException;
import com.wezaam.withdrawal.integration.MockedWithdrawal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {Application.class, H2JpaConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class WithdrawalServiceShould {

    @Autowired
    WithdrawalRepository withdrawalRepository;

    @Autowired
    WithdrawalService withdrawalService;

    @Test
    public void notCreateWithdrawalWhenUserDoesNotExists() {
        final CreateWithdrawalCommand createWithdrawalCommand = CreateWithdrawalCommandBuilder.aCreateWithdrawalCommandBuilder()
                .withAmount(MockedWithdrawal.AVAILABLE.amount)
                .withUserId(MockedWithdrawal.UNAVAILABLE.userId)
                .withPaymentMethodId(MockedWithdrawal.UNAVAILABLE.paymentId)
                .withImmediate(Boolean.TRUE)
                .build();

        assertThrows(UserDoesNotExistsException.class, () -> {
            withdrawalService.createWithdrawal(createWithdrawalCommand);
        });
    }

    @Test
    public void notCreateWithdrawalWhenUserDoesNotHavePaymentMethod() {
        final CreateWithdrawalCommand createWithdrawalCommand = CreateWithdrawalCommandBuilder.aCreateWithdrawalCommandBuilder()
                .withAmount(MockedWithdrawal.AVAILABLE.amount)
                .withUserId(MockedWithdrawal.AVAILABLE.userId)
                .withPaymentMethodId(MockedWithdrawal.UNAVAILABLE.paymentId)
                .withImmediate(Boolean.TRUE)
                .build();

        assertThrows(InvalidPaymentMethodException.class, () -> {
            withdrawalService.createWithdrawal(createWithdrawalCommand);
        });
    }

    @Test
    public void notCreateWithdrawalWhenUserDoesNotHaveEnoughMoney() {
        final CreateWithdrawalCommand createWithdrawalCommand = CreateWithdrawalCommandBuilder.aCreateWithdrawalCommandBuilder()
                .withAmount(MockedWithdrawal.UNAVAILABLE.amount)
                .withUserId(MockedWithdrawal.AVAILABLE.userId)
                .withPaymentMethodId(MockedWithdrawal.AVAILABLE.paymentId)
                .withImmediate(Boolean.TRUE)
                .build();

        assertThrows(InsufficientAmountException.class, () -> {
            withdrawalService.createWithdrawal(createWithdrawalCommand);
        });
    }

    @Test
    public void createWithdrawalForUserwithEnoughMoneyOnHisPaymentMethod() {
        final CreateWithdrawalCommand createWithdrawalCommand =
                CreateWithdrawalCommandBuilder.aCreateWithdrawalCommandBuilder()
                        .withAmount(MockedWithdrawal.AVAILABLE.amount)
                        .withUserId(MockedWithdrawal.AVAILABLE.userId)
                        .withPaymentMethodId(MockedWithdrawal.AVAILABLE.paymentId)
                        .withImmediate(Boolean.TRUE)
                        .build();

        try {
            final Withdrawal createdWithdrawal = withdrawalService
                    .createWithdrawal(createWithdrawalCommand);

            final Withdrawal persistedWithdrawal = withdrawalRepository
                    .findById(createdWithdrawal.getId())
                    .orElseThrow();

            assertEquals(MockedWithdrawal.AVAILABLE.amount, persistedWithdrawal.getAmount());
            assertEquals(MockedWithdrawal.AVAILABLE.userId, persistedWithdrawal.getUser().getId());
            assertEquals(MockedWithdrawal.AVAILABLE.paymentId, persistedWithdrawal.getPaymentMethod().getId());
            assertEquals(WithdrawalStatus.PENDING, persistedWithdrawal.getWithdrawalStatus());
        } catch (Exception e) {
            fail(e);
        }
    }

}
