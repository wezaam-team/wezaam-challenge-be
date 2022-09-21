package com.wezaam.withdrawal.dto

import com.wezaam.withdrawal.enums.WithdrawalExecutionMethod
import java.time.Instant

data class CreateWithdrawalRequest(

    val userId: Long,
    val paymentMethodId: Long,
    val amount: Double,
    val executeAt: Instant? = null,
    val withdrawalExecutionMethod: WithdrawalExecutionMethod

)
