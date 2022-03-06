package com.weezam.challenge.user.domain.aggregate;

import com.weezam.challenge.user.domain.PaymentMethodAggregate;
import com.weezam.challenge.user.domain.exception.InvalidCriteriaException;
import com.weezam.challenge.user.domain.exception.PaymentMethodNotFoundException;
import com.weezam.challenge.user.domain.model.PaymentMethod;
import com.weezam.challenge.user.domain.repository.PaymentMethodRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@AllArgsConstructor
@Slf4j
public class DefaultPaymentMethodAggregate implements PaymentMethodAggregate {

    private final PaymentMethodRepository paymentMethodRepository;

    @Override
    public PaymentMethod findOne(final Long id) throws PaymentMethodNotFoundException, InvalidCriteriaException {
        if (Objects.isNull(id)) {
            throw new InvalidCriteriaException(String.format("PaymentMethod not found for criteria: '%d'", id));
        }
        return paymentMethodRepository.findOne(id).orElseThrow(() -> new PaymentMethodNotFoundException(String.format("PaymentMethod not found for criteria: '%d'", id)));
    }

    
    
}
