package com.wezaam.withdrawal.dto

import com.wezaam.withdrawal.model.PaymentMethod

data class UserDTO (
        val id:Long,
        val name:String,
        val paymentMethods: List<PaymentMethod?>? = null,
        var maxWithdrawalAmount: Double? = null )
