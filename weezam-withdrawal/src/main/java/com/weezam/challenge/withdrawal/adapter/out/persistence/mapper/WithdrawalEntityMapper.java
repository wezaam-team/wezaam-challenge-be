package com.weezam.challenge.withdrawal.adapter.out.persistence.mapper;

import com.weezam.challenge.withdrawal.adapter.out.persistence.entity.WithdrawalEntity;
import com.weezam.challenge.withdrawal.domain.model.Withdrawal;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WithdrawalEntityMapper extends EntityMapper<Withdrawal, WithdrawalEntity> {
}
