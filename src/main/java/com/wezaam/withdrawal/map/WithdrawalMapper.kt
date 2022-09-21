package com.wezaam.withdrawal.map

import com.wezaam.withdrawal.dto.CreateWithdrawalRequest
import com.wezaam.withdrawal.model.WithdrawalAsap
import com.wezaam.withdrawal.model.WithdrawalScheduled
import com.wezaam.withdrawal.model.WithdrawalStatus
import java.time.Instant
import org.springframework.stereotype.Component

@Component
class WithdrawalMapper {
    fun toWithdrawal(createWithdrawalRequest: CreateWithdrawalRequest): WithdrawalAsap {
        return WithdrawalAsap().apply {
            userId = createWithdrawalRequest.userId
            paymentMethodId = createWithdrawalRequest.paymentMethodId
            amount = createWithdrawalRequest.amount
            createdAt = Instant.now()
            status = WithdrawalStatus.PENDING
        }
    }

    fun toScheduledWithdrawal(createWithdrawalRequest: CreateWithdrawalRequest): WithdrawalScheduled {
        return WithdrawalScheduled().apply {
            userId = createWithdrawalRequest.userId
            paymentMethodId = createWithdrawalRequest.paymentMethodId
            amount = createWithdrawalRequest.amount
            createdAt = Instant.now()
            executeAt = createWithdrawalRequest.executeAt
            status = WithdrawalStatus.PENDING
        }
    }
}