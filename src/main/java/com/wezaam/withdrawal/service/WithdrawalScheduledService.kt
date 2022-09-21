package com.wezaam.withdrawal.service

import com.wezaam.withdrawal.model.WithdrawalScheduled
import com.wezaam.withdrawal.repository.WithdrawalScheduledRepository
import com.wezaam.withdrawal.service.notification.NotificationService
import com.wezaam.withdrawal.service.payment.PaymentMethodService
import com.wezaam.withdrawal.service.status.ProcessRequestStatus
import java.time.Instant
import org.springframework.stereotype.Service

@Service
class WithdrawalScheduledService(
    private val paymentMethodService: PaymentMethodService,
    private val notificationService: NotificationService,
    private val withdrawalScheduledRepository: WithdrawalScheduledRepository,
    private val processRequestStatus: ProcessRequestStatus,
) : WithdrawalService<WithdrawalScheduled> {
    override fun create(withdrawal: WithdrawalScheduled): WithdrawalScheduled {
        val save = withdrawalScheduledRepository.save(withdrawal)
        notificationService.send(withdrawal)
        return save
    }

    override fun findAll(): List<WithdrawalScheduled> = withdrawalScheduledRepository.findAll()

    fun process(withdrawal: WithdrawalScheduled) {
        paymentMethodService.findById(withdrawal.paymentMethodId).orElse(null)?.let {
            processRequestStatus.process(withdrawal, it)
            withdrawalScheduledRepository.save(withdrawal)
        }
    }

    fun findAllByExecuteAtBefore(instant: Instant): MutableList<WithdrawalScheduled> = withdrawalScheduledRepository.findAllByExecuteAtBefore(instant)
}