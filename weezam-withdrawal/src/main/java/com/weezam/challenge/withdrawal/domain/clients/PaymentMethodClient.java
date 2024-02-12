package com.weezam.challenge.withdrawal.domain.clients;

import com.weezam.challenge.withdrawal.domain.exception.PaymentMethodNotFoundException;
import com.weezam.challenge.withdrawal.domain.model.PaymentMethod;

import java.util.Optional;

public interface PaymentMethodClient {

    Optional<PaymentMethod> findOne(final Long id) throws PaymentMethodNotFoundException;
}
