package com.wezaam.withdrawal.infrastructure.dataaccess.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.wezaam.withdrawal.domain.entity.PaymentMethod;
import com.wezaam.withdrawal.domain.ports.output.repository.PaymentMethodRepository;
import com.wezaam.withdrawal.infrastructure.dataaccess.mapper.PaymentMethodDataAccessMapper;
import com.wezaam.withdrawal.infrastructure.dataaccess.repository.PaymentMethodJpaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentMethodRepositoryImpl implements PaymentMethodRepository {

    private final PaymentMethodJpaRepository paymentMethodRepository;
    private final PaymentMethodDataAccessMapper paymentMethodDataAccessMapper;

    @Override
    public Optional<PaymentMethod> findById(Long paymentMethodId) {
        return paymentMethodRepository
                .findById(paymentMethodId)
                .map(paymentMethodDataAccessMapper::mapAsPaymentMethod);
    }
}
