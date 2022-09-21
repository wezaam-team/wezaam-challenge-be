package com.wezaam.withdrawal.service

import com.wezaam.withdrawal.exception.TransactionException
import com.wezaam.withdrawal.model.WithdrawalScheduled
import com.wezaam.withdrawal.model.WithdrawalStatus
import org.springframework.stereotype.Service

@Service
class ProcessWithdrawalScheduledService(
    private val paymentMethodService: PaymentMethodService,
    private val eventsService: EventsService,
    private val withdrawalScheduledService: WithdrawalScheduledService,
    private val withdrawalProcessingService: WithdrawalProcessingService
) {

    fun process(withdrawal: WithdrawalScheduled) {
        val paymentMethod = paymentMethodService.findById(withdrawal.paymentMethodId)

        if (paymentMethod.isPresent) {
            try {
                withdrawal.status = WithdrawalStatus.PROCESSING
                withdrawal.transactionId = withdrawalProcessingService.sendToProcessing(withdrawal.amount, paymentMethod.get())
            } catch (e: TransactionException) {
                withdrawal.status = WithdrawalStatus.FAILED
            } catch (e: Exception) {
                withdrawal.status = WithdrawalStatus.INTERNAL_ERROR
            } finally {
                withdrawalScheduledService.save(withdrawal)
                eventsService.send(withdrawal)
            }
        }
    }
}