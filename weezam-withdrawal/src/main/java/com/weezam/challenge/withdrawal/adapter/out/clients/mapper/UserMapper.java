package com.weezam.challenge.withdrawal.adapter.out.clients.mapper;

import com.weezam.challenge.withdrawal.adapter.out.clients.dto.UserDto;
import com.weezam.challenge.withdrawal.domain.model.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    User toDomain(UserDto dto);

    UserDto toDto(User user);

}
