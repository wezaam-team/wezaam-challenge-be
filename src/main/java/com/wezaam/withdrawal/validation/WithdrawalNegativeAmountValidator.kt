package com.wezaam.withdrawal.validation

import com.wezaam.withdrawal.dto.CreateWithdrawalRequest
import com.wezaam.withdrawal.exception.BadRequestException
import org.springframework.stereotype.Component

@Component
class WithdrawalNegativeAmountValidator : CreateWithdrawalRequestValidator {
    override fun validate(createWithdrawalRequest: CreateWithdrawalRequest) {
        if (createWithdrawalRequest.amount < 0) throw BadRequestException("Amount not allowed")
    }
}