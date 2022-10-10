package com.wezaam.withdrawal.domain.model;

import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserTest {

    private List<PaymentMethod> paymentMethods;

    private User user;

    private static final long USER_ID = 100L;
    private static final String FIRST_NAME = "FIRST_NAME";
    private static final long PAYMENT_METHOD_ID = 200L;
    private static final double MAX_WITHDRAWAL_AMOUNT = 300d;

    private static final long WITHDRAWAL_ID = 400L;
    private static final double WITHDRAWAL_AMOUNT = MAX_WITHDRAWAL_AMOUNT - 1;
    private static final Instant WITHDRAWAL_EXECUTE_AT = Instant.parse("2023-01-01T00:00:00Z");

    @BeforeEach
    void setUp() {
        var mockedPaymentMethod = mock(PaymentMethod.class);

        when(mockedPaymentMethod.getId()).thenReturn(PAYMENT_METHOD_ID);

        this.paymentMethods = List.of(mockedPaymentMethod);

        this.user = new User(USER_ID, FIRST_NAME, this.paymentMethods, MAX_WITHDRAWAL_AMOUNT);
    }

    @Test
    void constructorShouldFailWhenFirstNameIsNull() {
        // Act, Assert
        var exception = assertThrows(IllegalArgumentException.class, () ->
                new User(USER_ID, null, this.paymentMethods, MAX_WITHDRAWAL_AMOUNT));

        assertEquals("The firstName should not be null.", exception.getMessage());
    }

    @Test
    void constructorShouldInitializePaymentMethodsWithAnEmptyListWhenPaymentMethodsIsNull() {
        // Act
        this.user = new User(USER_ID, FIRST_NAME, null, MAX_WITHDRAWAL_AMOUNT);

        // Assert
        assertNotNull(this.user.getPaymentMethods());
        assertTrue(this.user.getPaymentMethods().isEmpty());
    }

    @Test
    void constructorShouldInitializeState() {
        // Act
        this.user = new User(USER_ID, FIRST_NAME, this.paymentMethods, MAX_WITHDRAWAL_AMOUNT);

        // Assert
        assertAll(
                () -> assertEquals(USER_ID, user.getId()),
                () -> assertEquals(FIRST_NAME, user.getFirstName()),
                () -> assertEquals(this.paymentMethods, user.getPaymentMethods()),
                () -> assertEquals(MAX_WITHDRAWAL_AMOUNT, user.getMaxWithdrawalAmount()));
    }

    @Test
    void createWithdrawalShouldFailWhenAmountExceedsMaxWithdrawalAmount() {
        // Act, Assert
        var exception = assertThrows(IllegalArgumentException.class, () ->
                this.user.createWithdrawal(WITHDRAWAL_ID, PAYMENT_METHOD_ID, MAX_WITHDRAWAL_AMOUNT + 1, null));

        assertEquals("Amount: 301.00 exceeds max withdrawal amount: 300.00", exception.getMessage());
    }

    @Test
    void createWithdrawalShouldFailWhenUserDoesNotHavePaymentMethod() {
        // Arrange
        var missingPaymentMethodId = PAYMENT_METHOD_ID + 1;

        // Act, Assert
        var exception = assertThrows(IllegalArgumentException.class, () ->
                this.user.createWithdrawal(WITHDRAWAL_ID, missingPaymentMethodId, MAX_WITHDRAWAL_AMOUNT, null));

        assertEquals("User of ID: " + USER_ID + " does not have payment method of ID: " + missingPaymentMethodId + ".",
                     exception.getMessage());
    }

    @Test
    void createWithdrawalShouldCreateImmediateWithdrawal() {
        // Act
        var withdrawal = user.createWithdrawal(WITHDRAWAL_ID, PAYMENT_METHOD_ID, WITHDRAWAL_AMOUNT, null);

        // Assert
        assertAll(
                () -> assertEquals(WITHDRAWAL_ID, withdrawal.getId()),
                () -> assertEquals(USER_ID, withdrawal.getUserId()),
                () -> assertEquals(PAYMENT_METHOD_ID, withdrawal.getPaymentMethodId()),
                () -> assertEquals(WITHDRAWAL_AMOUNT, withdrawal.getAmount()),
                () -> assertNotNull(withdrawal.getCreatedAt()),
                () -> assertNull(withdrawal.getExecuteAt()),
                () -> assertEquals(WithdrawalStatus.PENDING, withdrawal.getStatus()));
    }

    @Test
    void createWithdrawalShouldCreateScheduledWithdrawal() {
        // Act
        var withdrawal =
                user.createWithdrawal(WITHDRAWAL_ID, PAYMENT_METHOD_ID, WITHDRAWAL_AMOUNT, WITHDRAWAL_EXECUTE_AT);

        // Assert
        assertAll(
                () -> assertEquals(WITHDRAWAL_ID, withdrawal.getId()),
                () -> assertEquals(USER_ID, withdrawal.getUserId()),
                () -> assertEquals(PAYMENT_METHOD_ID, withdrawal.getPaymentMethodId()),
                () -> assertEquals(WITHDRAWAL_AMOUNT, withdrawal.getAmount()),
                () -> assertNotNull(withdrawal.getCreatedAt()),
                () -> assertEquals(WITHDRAWAL_EXECUTE_AT, withdrawal.getExecuteAt()),
                () -> assertEquals(WithdrawalStatus.PENDING, withdrawal.getStatus()));
    }
}
