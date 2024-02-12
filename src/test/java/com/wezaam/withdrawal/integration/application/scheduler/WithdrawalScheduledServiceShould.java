package com.wezaam.withdrawal.integration.application.scheduler;

import com.wezaam.withdrawal.Application;
import com.wezaam.withdrawal.config.infrastructure.H2JpaConfig;
import com.wezaam.withdrawal.domain.*;
import com.wezaam.withdrawal.infrastructure.config.SchedulerConfig;
import com.wezaam.withdrawal.integration.MockedWithdrawal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {Application.class, H2JpaConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class WithdrawalScheduledServiceShould {

    @Autowired
    WithdrawalRepository withdrawalRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PaymentMethodRepository paymentMethodRepository;

    @Test
    public void processScheduleWithdrawals() {
        final int amountOfScheduleWithdrawals = 5;
        final int extraTimeFromNowToScheduleTheWithrawalsInSeconds = 2;

        final long timeToWaitForTheScheduledTime = SchedulerConfig.DELAY_BETWEEN_EXECUTIONS;

        Map<Long, Withdrawal> scheduleWithdrawals =
                givenTotalOfWithdrawalsToBeScheduledAfter(amountOfScheduleWithdrawals,
                        extraTimeFromNowToScheduleTheWithrawalsInSeconds)
                        .stream()
                        .collect(Collectors.toMap(Withdrawal::getId, Function.identity()));

        waitFor(timeToWaitForTheScheduledTime);

        assertEquals(
                amountOfScheduleWithdrawals,
                withdrawalRepository
                        .findAll()
                        .stream()
                        .filter(withdrawal ->
                                scheduleWithdrawals.containsKey(withdrawal.getId())
                        ).
                        filter(withdrawal ->
                                withdrawal.getWithdrawalStatus()
                                        .equals(WithdrawalStatus.SUCCESS)
                        )
                        .count()
        );
    }

    private List<Withdrawal> givenTotalOfWithdrawalsToBeScheduledAfter(int totalOfWidrawals, int timeToBeScheduledInSeconds) {
        final Instant scheduleTimeForTheWithdrawals = Instant.now()
                .plus(timeToBeScheduledInSeconds, ChronoUnit.SECONDS);

        final List<Withdrawal> withdrawals = new ArrayList<>(totalOfWidrawals);
        for (int i = 0; i < totalOfWidrawals; i++) {
            withdrawals.add(
                    givenCreatedWithdrawal(
                            userRepository.getOne(MockedWithdrawal.AVAILABLE.userId),
                            WithdrawalStatus.PENDING,
                            paymentMethodRepository.getOne(MockedWithdrawal.AVAILABLE.paymentId),
                            MockedWithdrawal.AVAILABLE.amount,
                            scheduleTimeForTheWithdrawals)
            );
        }
        return withdrawals;
    }

    private void waitFor(long timeToWaitForTheScheduledTime) {
        try {
            Thread.sleep(timeToWaitForTheScheduledTime);
        } catch (InterruptedException e) {
        }
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
