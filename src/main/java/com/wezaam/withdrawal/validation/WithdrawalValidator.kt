package com.wezaam.withdrawal.validation

import com.wezaam.withdrawal.dto.CreateWithdrawalRequest
import com.wezaam.withdrawal.enums.WithdrawalExecutionMethod
import com.wezaam.withdrawal.exception.BadRequestException
import com.wezaam.withdrawal.exception.NotFoundException
import com.wezaam.withdrawal.service.PaymentMethodService
import com.wezaam.withdrawal.service.UserService
import java.time.Instant
import org.springframework.stereotype.Component

@Component
class WithdrawalValidator(private val userService: UserService, private val paymentMethodService: PaymentMethodService) {

    fun validate(createWithdrawalRequest: CreateWithdrawalRequest) {

        if (createWithdrawalRequest.withdrawalExecutionMethod == WithdrawalExecutionMethod.SCHEDULED) {
            if (createWithdrawalRequest.executeAt == null) throw BadRequestException("Scheduled method requires not null executeAt parameter")
            if (createWithdrawalRequest.executeAt.isBefore(Instant.now())) throw BadRequestException("Scheduled method requires date after executeAt parameter")
        }
        if (createWithdrawalRequest.amount < 0) throw BadRequestException("Amount not allowed")

        if (userService.findById(createWithdrawalRequest.userId).isEmpty) throw NotFoundException("User not found")

        if (paymentMethodService.findById(createWithdrawalRequest.paymentMethodId).isEmpty) throw throw NotFoundException("Payment method not found")


    }
}