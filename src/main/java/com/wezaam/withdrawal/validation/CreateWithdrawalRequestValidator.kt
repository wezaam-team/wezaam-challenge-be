package com.wezaam.withdrawal.validation

import com.wezaam.withdrawal.dto.CreateWithdrawalRequest

interface CreateWithdrawalRequestValidator {
    fun validate(createWithdrawalRequest: CreateWithdrawalRequest)
}