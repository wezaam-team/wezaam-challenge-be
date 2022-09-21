package com.wezaam.withdrawal.service.payment

import com.wezaam.withdrawal.model.PaymentMethod
import java.util.*

interface PaymentMethodService {
    fun findById(paymentMethodId: Long): Optional<PaymentMethod>
}