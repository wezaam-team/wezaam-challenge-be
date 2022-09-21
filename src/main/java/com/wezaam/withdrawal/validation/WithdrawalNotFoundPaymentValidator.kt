package com.wezaam.withdrawal.validation

import com.wezaam.withdrawal.dto.CreateWithdrawalRequest
import com.wezaam.withdrawal.exception.NotFoundException
import com.wezaam.withdrawal.service.payment.PaymentMethodService
import org.springframework.stereotype.Component

@Component
class WithdrawalNotFoundPaymentValidator(private val paymentMethodService: PaymentMethodService) : CreateWithdrawalRequestValidator {
    override fun validate(createWithdrawalRequest: CreateWithdrawalRequest) {
        if (paymentMethodService.findById(createWithdrawalRequest.paymentMethodId).isEmpty) throw throw NotFoundException("Payment method not found")
    }
}