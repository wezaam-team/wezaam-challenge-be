package com.weezam.challenge.user.adapter.out.persistence.mapper;

import com.weezam.challenge.user.adapter.out.persistence.entity.UserEntity;
import com.weezam.challenge.user.domain.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PaymentMethodEntityMapper.class})
public interface UserEntityMapper extends EntityMapper<User, UserEntity> {

    @Override
    User toDomain(UserEntity domain);

    @Override
    UserEntity toEntity(User dto);
}
