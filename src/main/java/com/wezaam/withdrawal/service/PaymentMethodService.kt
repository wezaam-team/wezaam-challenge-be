package com.wezaam.withdrawal.service

import com.wezaam.withdrawal.repository.PaymentMethodRepository
import org.springframework.stereotype.Service

@Service
class PaymentMethodService(private val paymentMethodRepository: PaymentMethodRepository) {

    fun findById(paymentMethodId: Long) = paymentMethodRepository.findById(paymentMethodId)

}