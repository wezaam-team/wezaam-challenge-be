package com.weezam.challenge.withdrawal.adapter.in.rest.mapper;

import com.weezam.challenge.withdrawal.adapter.in.rest.dto.NewWithdrawalCommandDto;
import com.weezam.challenge.withdrawal.adapter.in.rest.dto.WithdrawalDto;
import com.weezam.challenge.withdrawal.domain.model.Withdrawal;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WithdrawalMapper extends DomainMapper<WithdrawalDto, Withdrawal> {
    
}
