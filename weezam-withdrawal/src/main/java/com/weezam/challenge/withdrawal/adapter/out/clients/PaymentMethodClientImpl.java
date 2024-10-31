package com.weezam.challenge.withdrawal.adapter.out.clients;

import com.weezam.challenge.withdrawal.adapter.out.clients.dto.PaymentMethodDto;
import com.weezam.challenge.withdrawal.adapter.out.clients.mapper.PaymentMethodMapper;
import com.weezam.challenge.withdrawal.domain.clients.PaymentMethodClient;
import com.weezam.challenge.withdrawal.domain.exception.PaymentMethodNotFoundException;
import com.weezam.challenge.withdrawal.domain.model.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@AllArgsConstructor
@Slf4j
public class PaymentMethodClientImpl implements PaymentMethodClient {

    private final OpenFeignPaymentMethodClient openFeignPaymentMethodClient;
    private final PaymentMethodMapper paymentMethodMapper;

    @Override
    public Optional<PaymentMethod> findOne(Long id) throws PaymentMethodNotFoundException {
        try {
            PaymentMethodDto res = openFeignPaymentMethodClient.findOne(id);
            return Optional.ofNullable(paymentMethodMapper.toDomain(res));
        } catch (Exception exception) {
            log.error("Error getting PaymentMethod with id {}", id,  exception);
            throw new PaymentMethodNotFoundException(String.format("PaymentMethod with id {} no found", id));
        }
    }
}
