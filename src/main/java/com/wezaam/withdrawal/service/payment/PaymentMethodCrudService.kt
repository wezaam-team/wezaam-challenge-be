package com.wezaam.withdrawal.service.payment

import com.wezaam.withdrawal.repository.PaymentMethodRepository
import org.springframework.stereotype.Service

@Service
class PaymentMethodCrudService(private val paymentMethodRepository: PaymentMethodRepository) : PaymentMethodService {
    override fun findById(paymentMethodId: Long) = paymentMethodRepository.findById(paymentMethodId)

}