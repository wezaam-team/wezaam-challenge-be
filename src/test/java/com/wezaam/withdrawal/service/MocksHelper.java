package com.wezaam.withdrawal.service;

import com.wezaam.withdrawal.model.PaymentMethod;
import com.wezaam.withdrawal.model.User;

import java.util.Collections;

public class MocksHelper {

    public static User mockUser(Double maxWithdrawalAmount, Long paymentMethodId) {
        User user = new User();
        user.setFirstName("Test");
        user.setPaymentMethods(Collections.singletonList(mockPaymentMethod(paymentMethodId)));
        user.setMaxWithdrawalAmount(maxWithdrawalAmount);
        return user;
    }

    public static PaymentMethod mockPaymentMethod(Long paymentMethodId) {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setId(paymentMethodId);
        paymentMethod.setName("test bank account");
        return paymentMethod;
    }
}
