package com.wezaam.withdrawal.service

import com.wezaam.withdrawal.exception.TransactionException
import com.wezaam.withdrawal.model.WithdrawalScheduled
import com.wezaam.withdrawal.model.WithdrawalStatus
import com.wezaam.withdrawal.repository.WithdrawalScheduledRepository
import com.wezaam.withdrawal.service.notification.NotificationService
import com.wezaam.withdrawal.service.payment.PaymentMethodService
import com.wezaam.withdrawal.service.provider.WithdrawalProviderRequestService
import java.time.Instant
import org.springframework.stereotype.Service

@Service
class WithdrawalScheduledService(
    private val paymentMethodService: PaymentMethodService,
    private val notificationService: NotificationService,
    private val withdrawalScheduledRepository: WithdrawalScheduledRepository,
    private val withdrawalProviderRequestService: WithdrawalProviderRequestService,
) : WithdrawalService<WithdrawalScheduled> {
    override fun create(withdrawal: WithdrawalScheduled): WithdrawalScheduled {
        val save = withdrawalScheduledRepository.save(withdrawal)
        notificationService.send(withdrawal)
        return save
    }

    override fun findAll(): List<WithdrawalScheduled> = withdrawalScheduledRepository.findAll()

    fun process(withdrawal: WithdrawalScheduled) {
        paymentMethodService.findById(withdrawal.paymentMethodId).orElse(null)?.let {
            try {
                withdrawal.status = WithdrawalStatus.PROCESSING
                notifyStatus(withdrawal)
                withdrawal.transactionId = withdrawalProviderRequestService.sendToProcessing(withdrawal.amount, it)
                withdrawal.status = WithdrawalStatus.SUCCESS
            } catch (e: TransactionException) {
                withdrawal.status = WithdrawalStatus.FAILED
            } catch (e: Exception) {
                withdrawal.status = WithdrawalStatus.INTERNAL_ERROR
            } finally {
                notifyStatus(withdrawal)
            }
        }
    }

    fun findAllByExecuteAtBefore(instant: Instant): MutableList<WithdrawalScheduled> = withdrawalScheduledRepository.findAllByExecuteAtBefore(instant)

    private fun notifyStatus(withdrawal: WithdrawalScheduled) {
        notificationService.send(withdrawal)
        withdrawalScheduledRepository.save(withdrawal)
    }
}