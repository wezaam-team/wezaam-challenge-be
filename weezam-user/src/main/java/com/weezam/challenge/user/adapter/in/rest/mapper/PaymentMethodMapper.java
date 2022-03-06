package com.weezam.challenge.user.adapter.in.rest.mapper;

import com.weezam.challenge.user.adapter.in.rest.dto.PaymentMethodDto;
import com.weezam.challenge.user.domain.model.PaymentMethod;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface PaymentMethodMapper extends DomainMapper<PaymentMethodDto, PaymentMethod> {
    
}
