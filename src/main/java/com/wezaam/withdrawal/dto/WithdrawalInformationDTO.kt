package com.wezaam.withdrawal.dto

import com.wezaam.withdrawal.model.PaymentMethod
import com.wezaam.withdrawal.model.WithdrawType
import com.wezaam.withdrawal.model.WithdrawalStatus
import java.time.Instant
import javax.validation.constraints.NotNull

data class WithdrawalInformationDTO(
        val id: Long,
        val transactionId: Long,
        val amount: Double,
        val createdAt: Instant,
        val executeAt: Instant?,
        @NotNull(message = "User Id is mandatory")
        val userId: Long,
        @NotNull(message = "Payment MethodId is mandatory")
        val paymentMethod: PaymentMethod,
        val status: WithdrawalStatus,
        val type: WithdrawType
)
