package com.wezaam.withdrawal.integration.domain;

import com.wezaam.withdrawal.Application;
import com.wezaam.withdrawal.application.command.CreateWithdrawalCommand;
import com.wezaam.withdrawal.application.command.CreateWithdrawalCommandBuilder;
import com.wezaam.withdrawal.application.command.ProcessWithdrawalCommand;
import com.wezaam.withdrawal.application.command.ProcessWithdrawalCommandBuilder;
import com.wezaam.withdrawal.config.infrastructure.H2JpaConfig;
import com.wezaam.withdrawal.config.infrastructure.MockedEventPublisher;
import com.wezaam.withdrawal.config.infrastructure.MockedMQConfig;
import com.wezaam.withdrawal.domain.*;
import com.wezaam.withdrawal.domain.event.WithdrawalCreated;
import com.wezaam.withdrawal.domain.event.WithdrawalCreatedConverter;
import com.wezaam.withdrawal.domain.event.WithdrawalProcessed;
import com.wezaam.withdrawal.domain.event.WithdrawalProcessedConverter;
import com.wezaam.withdrawal.domain.exception.InsufficientAmountException;
import com.wezaam.withdrawal.domain.exception.InvalidPaymentMethodException;
import com.wezaam.withdrawal.domain.exception.UserDoesNotExistsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {Application.class, H2JpaConfig.class, MockedMQConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.main.allow-bean-definition-overriding=true",
                "test.rabbitMQConfig.enabled=false"}
)
public class WithdrawalServiceShould {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PaymentMethodRepository paymentMethodRepository;

    @Autowired
    WithdrawalRepository withdrawalRepository;

    @Autowired
    WithdrawalService withdrawalService;

    @Autowired
    private ApplicationContext context;

    private static final Long EXISTENT_USER_ID = 1L;

    private static final Long NON_EXISTENT_USER_ID = 2L;

    private static final Long EXISTENT_PAYMENT_METHOD_ID = 1L;

    private static final Long NON_EXISTENT_PAYMENT_METHOD_ID = 2L;

    private static final BigDecimal AVAILABLE_AMOUNT = BigDecimal.valueOf(1000, 2);

    private static final BigDecimal UNAVAILABLE_AMOUNT = BigDecimal.valueOf(1100, 2);

    @Test
    public void notCreateWithdrawalWhenUserDoesNotExists() {
        final CreateWithdrawalCommand createWithdrawalCommand = CreateWithdrawalCommandBuilder.aCreateWithdrawalCommandBuilder()
                .withAmount(AVAILABLE_AMOUNT)
                .withUserId(NON_EXISTENT_USER_ID)
                .withPaymentMethodId(NON_EXISTENT_PAYMENT_METHOD_ID)
                .withImmediate(Boolean.TRUE)
                .build();

        assertThrows(UserDoesNotExistsException.class, () -> {
            withdrawalService.createWithdrawal(createWithdrawalCommand);
        });
    }

    @Test
    public void notCreateWithdrawalWhenUserDoesNotHavePaymentMethod() {
        final CreateWithdrawalCommand createWithdrawalCommand = CreateWithdrawalCommandBuilder.aCreateWithdrawalCommandBuilder()
                .withAmount(AVAILABLE_AMOUNT)
                .withUserId(EXISTENT_USER_ID)
                .withPaymentMethodId(NON_EXISTENT_PAYMENT_METHOD_ID)
                .withImmediate(Boolean.TRUE)
                .build();

        assertThrows(InvalidPaymentMethodException.class, () -> {
            withdrawalService.createWithdrawal(createWithdrawalCommand);
        });
    }

    @Test
    public void notCreateWithdrawalWhenUserDoesNotHaveEnoughMoney() {
        final CreateWithdrawalCommand createWithdrawalCommand = CreateWithdrawalCommandBuilder.aCreateWithdrawalCommandBuilder()
                .withAmount(UNAVAILABLE_AMOUNT)
                .withUserId(EXISTENT_USER_ID)
                .withPaymentMethodId(EXISTENT_PAYMENT_METHOD_ID)
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
                        .withAmount(AVAILABLE_AMOUNT)
                        .withUserId(EXISTENT_USER_ID)
                        .withPaymentMethodId(EXISTENT_PAYMENT_METHOD_ID)
                        .withImmediate(Boolean.TRUE)
                        .build();

        try {
            final Withdrawal withdrawal = withdrawalService
                    .createWithdrawal(createWithdrawalCommand);

            assertEquals(AVAILABLE_AMOUNT, withdrawal.getAmount());
            assertEquals(EXISTENT_USER_ID, withdrawal.getUser().getId());
            assertEquals(EXISTENT_PAYMENT_METHOD_ID, withdrawal.getPaymentMethod().getId());

            assertCreatedEvent(
                    new WithdrawalCreatedConverter().apply(withdrawal)
            );

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void processWithdrawalShouldNotBeExecutedIfTheTimeHasNotArrived() {
        final Withdrawal createdWithdraw = givenCreatedWithdrawal();

        final ProcessWithdrawalCommand processWithdrawalCommand =
                ProcessWithdrawalCommandBuilder.aProcessWithdrawalCommandBuilder()
                        .withId(createdWithdraw.getId())
                        .withAmount(createdWithdraw.getAmount())
                        .withUserId(EXISTENT_USER_ID)
                        .withPaymentMethodId(EXISTENT_PAYMENT_METHOD_ID)
                        .withScheduledFor(Instant.now().plus(1, ChronoUnit.DAYS))
                        .withWithdrawalStatus(createdWithdraw.getWithdrawalStatus())
                        .build();

        try {
            withdrawalService
                    .processWithdrawal(processWithdrawalCommand);

            final Withdrawal processedWithdraw = withdrawalRepository
                    .findById(createdWithdraw.getId())
                    .orElseThrow();

            assertEquals(WithdrawalStatus.PENDING, processedWithdraw.getWithdrawalStatus());

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void processWithdrawalShouldBeExecutedIfTheTimeHasArrived() {
        final Withdrawal createdWithdraw = givenCreatedWithdrawal();

        final ProcessWithdrawalCommand processWithdrawalCommand =
                ProcessWithdrawalCommandBuilder.aProcessWithdrawalCommandBuilder()
                        .withId(createdWithdraw.getId())
                        .withAmount(createdWithdraw.getAmount())
                        .withUserId(EXISTENT_USER_ID)
                        .withPaymentMethodId(EXISTENT_PAYMENT_METHOD_ID)
                        .withScheduledFor(Instant.now().minus(5, ChronoUnit.MINUTES))
                        .withWithdrawalStatus(createdWithdraw.getWithdrawalStatus())
                        .build();

        try {
            withdrawalService
                    .processWithdrawal(processWithdrawalCommand);

            final Withdrawal processedWithdraw = withdrawalRepository
                    .findById(createdWithdraw.getId())
                    .orElseThrow();

            assertEquals(WithdrawalStatus.PROCESSING, processedWithdraw.getWithdrawalStatus());

            assertProcessedEvent(
                    new WithdrawalProcessedConverter().apply(processedWithdraw)
            );

        } catch (Exception e) {
            fail(e);
        }
    }

    private Withdrawal givenCreatedWithdrawal() {
        return withdrawalRepository.save(
                WithdrawalBuilder.aWithdrawalBuilder()
                        .withUser(userRepository.getOne(EXISTENT_USER_ID))
                        .withWithdrawalStatus(WithdrawalStatus.PENDING)
                        .withPaymentMethod(paymentMethodRepository.getOne(EXISTENT_PAYMENT_METHOD_ID))
                        .withAmount(AVAILABLE_AMOUNT)
                        .withScheduledFor(Instant.now())
                        .build()
        );
    }

    private void assertCreatedEvent(final WithdrawalCreated expectedEvent) {
        final WithdrawalCreated actualEvent = (WithdrawalCreated)
                getMockedEventPublisher()
                        .getCreatedEvents()
                        .stream()
                        .filter(event ->
                                ((WithdrawalCreated) event).getId() == expectedEvent.getId())
                        .findFirst().orElseThrow();

        assertEquals(expectedEvent.getId(), actualEvent.getId());
        assertEquals(expectedEvent.getPaymentMethodId(), actualEvent.getPaymentMethodId());
        assertEquals(expectedEvent.getAmount(), actualEvent.getAmount());
        assertEquals(expectedEvent.getUserId(), actualEvent.getUserId());

    }

    private void assertProcessedEvent(final WithdrawalProcessed expectedEvent) {
        final WithdrawalProcessed actualEvent = (WithdrawalProcessed)
                getMockedEventPublisher()
                        .getProcessedEvents()
                        .stream()
                        .filter(event ->
                                ((WithdrawalProcessed) event).getId() == expectedEvent.getId())
                        .findFirst().orElseThrow();

        assertEquals(expectedEvent.getId(), actualEvent.getId());
        assertEquals(expectedEvent.getPaymentMethodId(), actualEvent.getPaymentMethodId());
        assertEquals(expectedEvent.getAmount(), actualEvent.getAmount());
        assertEquals(expectedEvent.getUserId(), actualEvent.getUserId());

    }

    private MockedEventPublisher getMockedEventPublisher() {
        return (MockedEventPublisher) context.getBean(MockedEventPublisher.BEAN_NAME);
    }
}
