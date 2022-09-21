package com.wezaam.withdrawal.service.status

import com.wezaam.withdrawal.exception.TransactionException
import com.wezaam.withdrawal.model.PaymentMethod
import com.wezaam.withdrawal.model.Withdrawal
import com.wezaam.withdrawal.model.WithdrawalStatus
import com.wezaam.withdrawal.service.provider.WithdrawalProviderRequestService
import org.springframework.stereotype.Component

@Component
class ProcessRequestStatus(private val withdrawalProviderRequestService: WithdrawalProviderRequestService) {

    fun process(savedWithdrawal: Withdrawal, paymentMethod: PaymentMethod) {
        try {
            savedWithdrawal.status = WithdrawalStatus.PROCESSING
            savedWithdrawal.transactionId = withdrawalProviderRequestService.sendToProcessing(savedWithdrawal.amount, paymentMethod)
            savedWithdrawal.status = WithdrawalStatus.SUCCESS
        } catch (e: TransactionException) {
            savedWithdrawal.status = WithdrawalStatus.FAILED
        } catch (e: Exception) {
            savedWithdrawal.status = WithdrawalStatus.INTERNAL_ERROR
        }
    }
}