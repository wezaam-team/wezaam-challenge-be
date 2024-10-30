package com.wezaam.withdrawal.domain.model;

import com.wezaam.common.domain.model.DomainEventPublisher;
import com.wezaam.withdrawal.domain.service.WithdrawalProcessingService;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WithdrawalTest {

    private User mockedUser;
    private PaymentMethod mockedPaymentMethod;
    private UserRepository mockedUserRepository;
    private WithdrawalProcessingService mockedProcessingService;

    private Withdrawal withdrawal;

    private static final long WITHDRAWAL_ID = 100L;
    private static final long USER_ID = 200L;
    private static final long PAYMENT_METHOD_ID = 300L;
    private static final double AMOUNT = 400.00d;
    private static final long TRANSACTION_ID = 500L;
    private static final Instant CREATED_AT = Instant.parse("2022-01-01T00:00:00Z");
    private static final Instant EXECUTE_AT = Instant.parse("2022-02-01T00:00:00Z");

    private static final Instant OCCURRED_ON = CREATED_AT.plusSeconds(1);

    @BeforeEach
    void setUp() throws Exception {
        this.mockedUser = mock(User.class);
        this.mockedPaymentMethod = mock(PaymentMethod.class);
        this.mockedUserRepository = mock(UserRepository.class);
        this.mockedProcessingService = mock(WithdrawalProcessingService.class);

        when(this.mockedUser.hasPaymentMethodOfId(PAYMENT_METHOD_ID)).thenReturn(true);
        when(this.mockedUser.getPaymentMethodOfId(PAYMENT_METHOD_ID)).thenReturn(this.mockedPaymentMethod);

        when(this.mockedUserRepository.getUserOfId(USER_ID)).thenReturn(Optional.of(this.mockedUser));

        when(this.mockedProcessingService.sendToProcessing(AMOUNT, this.mockedPaymentMethod))
                .thenReturn(TRANSACTION_ID);

        this.withdrawal = new Withdrawal(WITHDRAWAL_ID, USER_ID, PAYMENT_METHOD_ID, AMOUNT, null);
    }

    @Test
    void constructorShouldInitializeStateAndPublishEvent() {
        // Arrange
        var domainEventCaptor = new DomainEventCaptor<>(WithdrawalCreated.class);

        DomainEventPublisher.instance().subscribe(domainEventCaptor);

        // Act
        try (var mockedInstantClass = mockStatic(Instant.class)) {
            mockedInstantClass.when(Instant::now).thenReturn(CREATED_AT, OCCURRED_ON);

            this.withdrawal = new Withdrawal(WITHDRAWAL_ID, USER_ID, PAYMENT_METHOD_ID, AMOUNT, EXECUTE_AT);
        }

        // Assert
        assertAll(
                () -> assertEquals(WITHDRAWAL_ID, this.withdrawal.getId()),
                () -> assertEquals(USER_ID, this.withdrawal.getUserId()),
                () -> assertEquals(PAYMENT_METHOD_ID, this.withdrawal.getPaymentMethodId()),
                () -> assertEquals(AMOUNT, this.withdrawal.getAmount()),
                () -> assertEquals(CREATED_AT, this.withdrawal.getCreatedAt()),
                () -> assertEquals(EXECUTE_AT, this.withdrawal.getExecuteAt()),
                () -> assertEquals(WithdrawalStatus.PENDING, this.withdrawal.getStatus()));

        assertEquals(1, domainEventCaptor.getCapturedDomainEvents().size());

        var publishedEvent = domainEventCaptor.getCapturedDomainEvents().get(0);

        assertAll(
                () -> assertEquals(WITHDRAWAL_ID, publishedEvent.getWithdrawalId()),
                () -> assertEquals(USER_ID, publishedEvent.getUserId()),
                () -> assertEquals(PAYMENT_METHOD_ID, publishedEvent.getPaymentMethodId()),
                () -> assertEquals(AMOUNT, publishedEvent.getWithdrawalAmount()),
                () -> assertEquals(CREATED_AT, publishedEvent.getWithdrawalCreatedAt()),
                () -> assertEquals(EXECUTE_AT, publishedEvent.getWithdrawalExecuteAt()),
                () -> assertEquals(WithdrawalStatus.PENDING, publishedEvent.getWithdrawalStatus()),
                () -> assertEquals(OCCURRED_ON, publishedEvent.getOccurredOn()));
    }

    @Test
    void triggerProcessingShouldFailWhenTheWithdrawalCanNotBeProcessed() {
        // Arrange
        this.withdrawal = new Withdrawal(WITHDRAWAL_ID, USER_ID, PAYMENT_METHOD_ID, AMOUNT, Instant.MAX);

        // Act, Assert
        var exception = assertThrows(IllegalStateException.class, () ->
                this.withdrawal.triggerProcessing(this.mockedProcessingService, this.mockedUserRepository));

        assertEquals("Withdrawal of ID: " + WITHDRAWAL_ID + " can not be processed.", exception.getMessage());
    }

    @Test
    void triggerProcessingShouldFailWhenTheUserDoesNotExist() {
        // Arrange
        when(this.mockedUserRepository.getUserOfId(USER_ID)).thenReturn(Optional.empty());

        // Act, Assert
        var exception = assertThrows(IllegalStateException.class, () ->
                this.withdrawal.triggerProcessing(this.mockedProcessingService, this.mockedUserRepository));

        assertEquals("User of ID: " + USER_ID + " does not exist.", exception.getMessage());
    }

    @Test
    void triggerProcessingShouldFailWhenTheUserDoesNotHaveThePaymentMethod() {
        // Arrange
        when(this.mockedUser.hasPaymentMethodOfId(PAYMENT_METHOD_ID)).thenReturn(false);

        // Act, Assert
        var exception = assertThrows(IllegalStateException.class, () ->
                this.withdrawal.triggerProcessing(this.mockedProcessingService, this.mockedUserRepository));

        assertEquals("User of ID: " + USER_ID + " does not have payment method of ID: " + PAYMENT_METHOD_ID + ".",
                     exception.getMessage());
    }

    @Test
    void triggerProcessingShouldUpdateStateAndPublishEventWhenTriggeringSucceeds() throws Exception {
        // Arrange
        var domainEventCaptor = new DomainEventCaptor<>(WithdrawalProcessingTriggered.class);

        DomainEventPublisher.instance().subscribe(domainEventCaptor);

        // Act
        try (var mockedInstantClass = mockStatic(Instant.class)) {
            mockedInstantClass.when(Instant::now).thenReturn(OCCURRED_ON);

            this.withdrawal.triggerProcessing(this.mockedProcessingService, this.mockedUserRepository);
        }

        // Assert
        assertAll(
                () -> assertEquals(this.withdrawal.getTransactionId(), TRANSACTION_ID),
                () -> assertEquals(this.withdrawal.getStatus(), WithdrawalStatus.PROCESSING));

        verify(this.mockedUserRepository).getUserOfId(USER_ID);
        verify(this.mockedProcessingService).sendToProcessing(AMOUNT, this.mockedPaymentMethod);

        assertEquals(1, domainEventCaptor.getCapturedDomainEvents().size());

        var publishedEvent = domainEventCaptor.getCapturedDomainEvents().get(0);

        assertAll(
                () -> assertEquals(WITHDRAWAL_ID, publishedEvent.getWithdrawalId()),
                () -> assertEquals(TRANSACTION_ID, publishedEvent.getTransactionId()),
                () -> assertEquals(WithdrawalStatus.PROCESSING, publishedEvent.getWithdrawalStatus()),
                () -> assertEquals(OCCURRED_ON, publishedEvent.getOccurredOn()));
    }

    @Test
    void triggerProcessingShouldUpdateStatusAndPublishEventWhenTriggeringFailsWithTransactionException() throws Exception {
        // Arrange
        var domainEventCaptor = new DomainEventCaptor<>(WithdrawalProcessingTriggeringFailed.class);

        DomainEventPublisher.instance().subscribe(domainEventCaptor);

        when(this.mockedProcessingService.sendToProcessing(AMOUNT, this.mockedPaymentMethod))
                .thenThrow(new TransactionException());

        // Act
        try (var mockedInstantClass = mockStatic(Instant.class)) {
            mockedInstantClass.when(Instant::now).thenReturn(OCCURRED_ON);

            this.withdrawal.triggerProcessing(this.mockedProcessingService, this.mockedUserRepository);
        }

        // Assert
        assertEquals(this.withdrawal.getStatus(), WithdrawalStatus.FAILED);

        verify(this.mockedUserRepository).getUserOfId(USER_ID);
        verify(this.mockedProcessingService).sendToProcessing(AMOUNT, this.mockedPaymentMethod);

        assertEquals(1, domainEventCaptor.getCapturedDomainEvents().size());

        var publishedEvent = domainEventCaptor.getCapturedDomainEvents().get(0);

        assertAll(
                () -> assertEquals(WITHDRAWAL_ID, publishedEvent.getWithdrawalId()),
                () -> assertNull(publishedEvent.getFailureMessage()),
                () -> assertEquals(WithdrawalStatus.FAILED, publishedEvent.getWithdrawalStatus()),
                () -> assertEquals(OCCURRED_ON, publishedEvent.getOccurredOn()));
    }

    @Test
    void triggerProcessingShouldUpdateStatusAndPublishEventWhenTriggeringFailsWithOtherException() throws Exception {
        // Arrange
        var domainEventCaptor = new DomainEventCaptor<>(WithdrawalProcessingTriggeringFailed.class);

        DomainEventPublisher.instance().subscribe(domainEventCaptor);

        var failureMessage = "An error occurred while triggering the processing of the withdrawal.";

        when(this.mockedProcessingService.sendToProcessing(AMOUNT, this.mockedPaymentMethod))
                .thenThrow(new RuntimeException(failureMessage));

        // Act
        try (var mockedInstantClass = mockStatic(Instant.class)) {
            mockedInstantClass.when(Instant::now).thenReturn(OCCURRED_ON);

            this.withdrawal.triggerProcessing(this.mockedProcessingService, this.mockedUserRepository);
        }

        // Assert
        assertEquals(this.withdrawal.getStatus(), WithdrawalStatus.INTERNAL_ERROR);

        verify(this.mockedUserRepository).getUserOfId(USER_ID);
        verify(this.mockedProcessingService).sendToProcessing(AMOUNT, this.mockedPaymentMethod);

        assertEquals(1, domainEventCaptor.getCapturedDomainEvents().size());

        var publishedEvent = domainEventCaptor.getCapturedDomainEvents().get(0);

        assertAll(
                () -> assertEquals(WITHDRAWAL_ID, publishedEvent.getWithdrawalId()),
                () -> assertEquals(failureMessage, publishedEvent.getFailureMessage()),
                () -> assertEquals(WithdrawalStatus.INTERNAL_ERROR, publishedEvent.getWithdrawalStatus()),
                () -> assertEquals(OCCURRED_ON, publishedEvent.getOccurredOn()));
    }
}
