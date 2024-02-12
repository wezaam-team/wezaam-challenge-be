package com.weezam.challenge.user.adapter.out.persistence;

import com.weezam.challenge.user.adapter.out.persistence.entity.PaymentMethodEntity;
import com.weezam.challenge.user.adapter.out.persistence.entity.UserEntity;
import com.weezam.challenge.user.adapter.out.persistence.mapper.PaymentMethodEntityMapper;
import com.weezam.challenge.user.adapter.out.persistence.mapper.UserEntityMapper;
import com.weezam.challenge.user.domain.model.PaymentMethod;
import com.weezam.challenge.user.domain.model.User;
import com.weezam.challenge.user.domain.repository.PaymentMethodRepository;
import com.weezam.challenge.user.domain.repository.UserRepository;
import lombok.AllArgsConstructor;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@AllArgsConstructor
public class DbPaymentMethodRepository implements PaymentMethodRepository {

    private final PaymentMethodRepositoryJpa paymentMethodRepositoryJpa;

    private final PaymentMethodEntityMapper paymentMethodEntityMapper;

    @Override
    public Optional<PaymentMethod> findOne(Long id) {
        PaymentMethodEntity entity = paymentMethodRepositoryJpa.getById(id);
        return Optional.ofNullable(paymentMethodEntityMapper.toDomain(entity));
    }
}
