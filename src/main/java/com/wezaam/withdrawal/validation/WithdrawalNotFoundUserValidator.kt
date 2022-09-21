package com.wezaam.withdrawal.validation

import com.wezaam.withdrawal.dto.CreateWithdrawalRequest
import com.wezaam.withdrawal.exception.NotFoundException
import com.wezaam.withdrawal.service.user.UserService
import org.springframework.stereotype.Component

@Component
class WithdrawalNotFoundUserValidator(private val userService: UserService) : CreateWithdrawalRequestValidator {
    override fun validate(createWithdrawalRequest: CreateWithdrawalRequest) {
        if (userService.findById(createWithdrawalRequest.userId).isEmpty) throw NotFoundException("User not found")
    }
}