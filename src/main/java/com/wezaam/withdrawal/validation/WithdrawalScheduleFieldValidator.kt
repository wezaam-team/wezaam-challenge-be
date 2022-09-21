package com.wezaam.withdrawal.validation

import com.wezaam.withdrawal.dto.CreateWithdrawalRequest
import com.wezaam.withdrawal.enums.WithdrawalExecutionMethod
import com.wezaam.withdrawal.exception.BadRequestException
import org.springframework.stereotype.Component

@Component
class WithdrawalScheduleFieldValidator : CreateWithdrawalRequestValidator {
    override fun validate(createWithdrawalRequest: CreateWithdrawalRequest) {
        if (createWithdrawalRequest.withdrawalExecutionMethod == WithdrawalExecutionMethod.SCHEDULED && createWithdrawalRequest.executeAt == null) {
            throw BadRequestException("Scheduled method requires not null executeAt parameter")
        }
    }
}