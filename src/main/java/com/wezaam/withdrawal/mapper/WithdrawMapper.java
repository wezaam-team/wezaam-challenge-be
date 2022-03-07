package com.wezaam.withdrawal.mapper;

import com.wezaam.withdrawal.domain.entities.Employee;
import com.wezaam.withdrawal.domain.entities.Withdraw;
import com.wezaam.withdrawal.rest.dto.WithdrawalRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WithdrawMapper {

    @Mapping(target = "amount", source = "withdrawalRequest.withdrawalAmount")
    @Mapping(target = "currency", source = "withdrawalRequest.currency")
    @Mapping(target = "paymentMethod", source = "withdrawalRequest.paymentMethod")
    Withdraw map(WithdrawalRequest withdrawalRequest, Employee employee);

}
