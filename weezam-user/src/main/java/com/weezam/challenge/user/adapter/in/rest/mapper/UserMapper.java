package com.weezam.challenge.user.adapter.in.rest.mapper;

import com.weezam.challenge.user.adapter.in.rest.dto.UserDto;
import com.weezam.challenge.user.domain.model.User;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PaymentMethodMapper.class})
public interface UserMapper extends DomainMapper<UserDto, User> {
    
}
