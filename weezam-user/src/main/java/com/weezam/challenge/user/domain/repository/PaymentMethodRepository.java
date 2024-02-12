package com.weezam.challenge.user.domain.repository;

import com.weezam.challenge.user.domain.model.PaymentMethod;

import java.util.Optional;

public interface PaymentMethodRepository {

    Optional<PaymentMethod> findOne(final Long id);
}
