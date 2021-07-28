package com.wezaam.withdrawal.integration.domain;

import com.wezaam.withdrawal.Application;
import com.wezaam.withdrawal.application.command.FinishWithdrawalProcessingCommandBuilder;
import com.wezaam.withdrawal.application.command.ProcessWithdrawalCommand;
import com.wezaam.withdrawal.application.command.ProcessWithdrawalCommandBuilder;
import com.wezaam.withdrawal.config.infrastructure.H2JpaConfig;
import com.wezaam.withdrawal.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest(classes = {Application.class, H2JpaConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class WithdrawalProcessingServiceShould {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PaymentMethodRepository paymentMethodRepository;

    @Autowired
    WithdrawalRepository withdrawalRepository;

    @Autowired
    private WithdrawalProcessingService withdrawalProcessingService;

    @Test
    public void notProcessWithdrawalIfTheTimeHasNotArrived() {
        final Withdrawal withdrawalCreatedForTomorrow = givenCreatedWithdrawal(
                userRepository.getOne(MockedWithdrawal.AVAILABLE.userId),
                WithdrawalStatus.PENDING,
                paymentMethodRepository.getOne(MockedWithdrawal.AVAILABLE.paymentId),
                MockedWithdrawal.AVAILABLE.amount,
                Instant.now().plus(1, ChronoUnit.DAYS)
        );

        final ProcessWithdrawalCommand processWithdrawalCommand =
                ProcessWithdrawalCommandBuilder.aProcessWithdrawalCommandBuilder()
                        .withId(withdrawalCreatedForTomorrow.getId())
                        .withAmount(withdrawalCreatedForTomorrow.getAmount())
                        .withUserId(MockedWithdrawal.AVAILABLE.userId)
                        .withPaymentMethodId(MockedWithdrawal.AVAILABLE.paymentId)
                        .withScheduledFor(Instant.now().plus(1, ChronoUnit.DAYS))
                        .withWithdrawalStatus(withdrawalCreatedForTomorrow.getWithdrawalStatus())
                        .build();

        try {
            withdrawalProcessingService
                    .processWithdrawal(processWithdrawalCommand);

            final Withdrawal processedWithdraw = withdrawalRepository
                    .findById(withdrawalCreatedForTomorrow.getId())
                    .orElseThrow();

            assertEquals(WithdrawalStatus.PENDING, processedWithdraw.getWithdrawalStatus());

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void processIfTheTimeHasArrived() {
        final Withdrawal createdWithdraw = givenCreatedWithdrawal();

        final ProcessWithdrawalCommand processWithdrawalCommand =
                ProcessWithdrawalCommandBuilder.aProcessWithdrawalCommandBuilder()
                        .withId(createdWithdraw.getId())
                        .withAmount(createdWithdraw.getAmount())
                        .withUserId(MockedWithdrawal.AVAILABLE.userId)
                        .withPaymentMethodId(MockedWithdrawal.AVAILABLE.paymentId)
                        .withScheduledFor(Instant.now().minus(5, ChronoUnit.MINUTES))
                        .withWithdrawalStatus(createdWithdraw.getWithdrawalStatus())
                        .build();

        try {
            withdrawalProcessingService
                    .processWithdrawal(processWithdrawalCommand);

            final Withdrawal processedWithdraw = withdrawalRepository
                    .findById(createdWithdraw.getId())
                    .orElseThrow();

            assertEquals(WithdrawalStatus.PROCESSING, processedWithdraw.getWithdrawalStatus());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void notProcessNotPendingWithdrawal() {
        final Withdrawal failedWithdrawal = givenCreatedWithdrawal(
                userRepository.getOne(MockedWithdrawal.AVAILABLE.userId),
                WithdrawalStatus.FAILED,
                paymentMethodRepository.getOne(MockedWithdrawal.AVAILABLE.paymentId),
                MockedWithdrawal.AVAILABLE.amount,
                Instant.now());

        try {
            withdrawalProcessingService.processWithdrawal(
                    ProcessWithdrawalCommandBuilder.aProcessWithdrawalCommandBuilder()
                            .withId(failedWithdrawal.getId())
                            .withAmount(failedWithdrawal.getAmount())
                            .withUserId(failedWithdrawal.getUser().getId())
                            .withPaymentMethodId(failedWithdrawal.getPaymentMethod().getId())
                            .withScheduledFor(failedWithdrawal.getScheduledFor())
                            .withWithdrawalStatus(failedWithdrawal.getWithdrawalStatus())
                            .build()
            );

            final Withdrawal notProcessedWithdraw = withdrawalRepository
                    .findById(failedWithdrawal.getId())
                    .orElseThrow();

            assertNotEquals(WithdrawalStatus.PROCESSING,
                    notProcessedWithdraw.getWithdrawalStatus());

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void notCloseNotProcessingWithdrawal() {
        final Withdrawal failedWithdrawal = givenCreatedWithdrawal(
                userRepository.getOne(MockedWithdrawal.AVAILABLE.userId),
                WithdrawalStatus.FAILED,
                paymentMethodRepository.getOne(MockedWithdrawal.AVAILABLE.paymentId),
                MockedWithdrawal.AVAILABLE.amount,
                Instant.now());

        try {
            withdrawalProcessingService.finishWithdrawalProcessing(
                    FinishWithdrawalProcessingCommandBuilder.aFinishWithdrawalProcessingCommandBuilder()
                            .withId(failedWithdrawal.getId())
                            .withAmount(failedWithdrawal.getAmount())
                            .withUserId(failedWithdrawal.getUser().getId())
                            .withPaymentMethodId(failedWithdrawal.getPaymentMethod().getId())
                            .withScheduledFor(failedWithdrawal.getScheduledFor())
                            .withWithdrawalStatus(failedWithdrawal.getWithdrawalStatus())
                            .build()
            );

            final Withdrawal notFinishedWithdrawal = withdrawalRepository
                    .findById(failedWithdrawal.getId())
                    .orElseThrow();

            assertNotEquals(WithdrawalStatus.SUCCESS,
                    notFinishedWithdrawal.getWithdrawalStatus());

        } catch (Exception e) {
            fail(e);
        }
    }

    private Withdrawal givenCreatedWithdrawal() {

        return givenCreatedWithdrawal(
                userRepository.getOne(MockedWithdrawal.AVAILABLE.userId),
                WithdrawalStatus.PENDING,
                paymentMethodRepository.getOne(MockedWithdrawal.AVAILABLE.paymentId),
                MockedWithdrawal.AVAILABLE.amount,
                Instant.now());
    }

    private Withdrawal givenCreatedWithdrawal(
            User user,
            WithdrawalStatus withdrawalStatus,
            PaymentMethod paymentMethod,
            BigDecimal amount,
            Instant scheduleFor) {

        return withdrawalRepository.save(
                WithdrawalBuilder.aWithdrawalBuilder()
                        .withUser(user)
                        .withWithdrawalStatus(withdrawalStatus)
                        .withPaymentMethod(paymentMethod)
                        .withAmount(amount)
                        .withScheduledFor(scheduleFor)
                        .build()
        );
    }
}
