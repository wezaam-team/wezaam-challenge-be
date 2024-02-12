package com.weezam.challenge.withdrawal.adapter.out.clients.mapper;

import com.weezam.challenge.withdrawal.adapter.out.clients.dto.PaymentMethodDto;
import com.weezam.challenge.withdrawal.domain.model.PaymentMethod;
import org.mapstruct.Mapper;

@Mapper
public interface PaymentMethodMapper {

    PaymentMethod toDomain(PaymentMethodDto dto);

    PaymentMethodDto toDto(PaymentMethod user);

}
