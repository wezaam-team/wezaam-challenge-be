package com.wezaam.withdrawal.domain.entity;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import com.wezaam.withdrawal.domain.exception.WithdrawalDomainException;
import com.wezaam.withdrawal.domain.valueobject.WithdrawalStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Withdrawal {

    private Long id;
    private Long transactionId;
    private Double amount;
    private Instant createdAt;
    private Long userId;
    private Long paymentMethodId;
    private WithdrawalStatus status;

    public void checkInitialWithdrawal(
            Double maxWithdrawalAmount, List<PaymentMethod> allowedPaymentMethodIds) {
        validateMaxWithdrawalAmount(maxWithdrawalAmount);
        validatePaymentMethod(allowedPaymentMethodIds);
    }

    private void validatePaymentMethod(List<PaymentMethod> allowedPaymentMethods) {
        if (isInvalidPaymentMethods(allowedPaymentMethods)) {
            throw new WithdrawalDomainException(
                    "Invalid payment method: No valid payment methods provided.");
        }

        if (!isPaymentMethodAllowed(allowedPaymentMethods)) {
            throw new WithdrawalDomainException(
                    "Invalid payment method: Not among allowed payment methods.");
        }
    }

    private void validateMaxWithdrawalAmount(Double maxWithdrawalAmount) {
        if (amountExceedsLimit(maxWithdrawalAmount)) {
            throw new WithdrawalDomainException(
                    "Withdrawal amount exceeds the maximum allowed limit of "
                            + maxWithdrawalAmount);
        }
    }

    private boolean isInvalidPaymentMethods(List<PaymentMethod> paymentMethodIds) {
        return Optional.ofNullable(paymentMethodIds).map(List::isEmpty).orElse(true);
    }

    private boolean isPaymentMethodAllowed(List<PaymentMethod> allowedPaymentMethodIds) {
        return allowedPaymentMethodIds.stream()
                .anyMatch(pm -> pm.getId().equals(this.paymentMethodId));
    }

    private boolean amountExceedsLimit(Double maxWithdrawalAmount) {
        return amount != null && maxWithdrawalAmount != null && amount > maxWithdrawalAmount;
    }
}
