package com.wezaam.withdrawal.service.provider

import com.wezaam.withdrawal.model.PaymentMethod

interface WithdrawalProviderRequestService {
    fun sendToProcessing(amount: Double?, paymentMethod: PaymentMethod?): Long?
}