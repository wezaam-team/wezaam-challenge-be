package com.weezam.challenge.user.domain;

import com.weezam.challenge.user.domain.exception.InvalidCriteriaException;
import com.weezam.challenge.user.domain.exception.PaymentMethodNotFoundException;
import com.weezam.challenge.user.domain.model.PaymentMethod;

public interface PaymentMethodAggregate {

    PaymentMethod findOne(final Long id) throws PaymentMethodNotFoundException, InvalidCriteriaException;

}
