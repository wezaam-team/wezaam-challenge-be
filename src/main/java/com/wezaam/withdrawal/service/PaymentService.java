package com.wezaam.withdrawal.service;

import com.wezaam.withdrawal.model.PaymentMethod;

import java.util.List;
import java.util.Optional;

public interface PaymentService {

    PaymentMethod findByName(String name) throws Exception;
    PaymentMethod findById(Long id) throws Exception;
    List<PaymentMethod> addPaymentMethods(List<PaymentMethod> paymentMehods);
    PaymentMethod addPaymentMethod(PaymentMethod paymentMethod);
    void deletePaymentMethod(PaymentMethod paymentMethod) throws Exception;
    void deletePaymentMethods(List<PaymentMethod> list);
}
