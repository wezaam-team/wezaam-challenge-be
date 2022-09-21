package com.wezaam.withdrawal.service

import com.wezaam.withdrawal.model.WithdrawalAsap
import com.wezaam.withdrawal.repository.WithdrawalRepository
import com.wezaam.withdrawal.service.notification.NotificationService
import com.wezaam.withdrawal.service.payment.PaymentMethodService
import com.wezaam.withdrawal.service.status.ProcessRequestStatus
import java.util.concurrent.Executors
import org.springframework.stereotype.Service

@Service
class WithdrawalAsapService(
    private val withdrawalRepository: WithdrawalRepository,
    private val paymentMethodService: PaymentMethodService,
    private val notificationService: NotificationService,
    private val processRequestStatus: ProcessRequestStatus,
) : WithdrawalService<WithdrawalAsap> {

    private val executorService = Executors.newCachedThreadPool()

    override fun create(withdrawal: WithdrawalAsap): WithdrawalAsap {
        val savedWithdrawal = withdrawalRepository.save(withdrawal)
        notificationService.send(savedWithdrawal)

        paymentMethodService.findById(savedWithdrawal.paymentMethodId).orElse(null)?.let {
            executorService.submit {
                processRequestStatus.process(savedWithdrawal, it)
                withdrawalRepository.save(savedWithdrawal)
            }
        }
        return savedWithdrawal
    }

    override fun findAll(): List<WithdrawalAsap> = withdrawalRepository.findAll()

}