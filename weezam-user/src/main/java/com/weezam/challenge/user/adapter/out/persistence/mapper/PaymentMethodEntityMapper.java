package com.weezam.challenge.user.adapter.out.persistence.mapper;

import com.weezam.challenge.user.adapter.out.persistence.entity.PaymentMethodEntity;
import com.weezam.challenge.user.domain.model.PaymentMethod;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface PaymentMethodEntityMapper extends EntityMapper<PaymentMethod, PaymentMethodEntity> {
    
}
