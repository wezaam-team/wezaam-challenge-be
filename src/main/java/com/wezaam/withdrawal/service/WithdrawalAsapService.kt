package com.wezaam.withdrawal.service

import com.wezaam.withdrawal.exception.TransactionException
import com.wezaam.withdrawal.model.WithdrawalAsap
import com.wezaam.withdrawal.model.WithdrawalStatus
import com.wezaam.withdrawal.repository.WithdrawalRepository
import com.wezaam.withdrawal.service.notification.NotificationService
import com.wezaam.withdrawal.service.payment.PaymentMethodService
import com.wezaam.withdrawal.service.provider.WithdrawalProviderRequestService
import java.util.concurrent.Executors
import org.springframework.stereotype.Service

@Service
class WithdrawalAsapService(
    private val withdrawalRepository: WithdrawalRepository,
    private val paymentMethodService: PaymentMethodService,
    private val notificationService: NotificationService,
    private val withdrawalProviderRequestService: WithdrawalProviderRequestService,
) : WithdrawalService<WithdrawalAsap> {

    private val executorService = Executors.newCachedThreadPool()

    override fun create(withdrawal: WithdrawalAsap): WithdrawalAsap {
        val savedWithdrawal = withdrawalRepository.save(withdrawal)
        notificationService.send(savedWithdrawal)

        paymentMethodService.findById(savedWithdrawal.paymentMethodId).orElse(null)?.let {
            executorService.submit {
                try {
                    savedWithdrawal.status = WithdrawalStatus.PROCESSING
                    notifyStatus(savedWithdrawal)
                    savedWithdrawal.transactionId = withdrawalProviderRequestService.sendToProcessing(savedWithdrawal.amount, it)
                    savedWithdrawal.status = WithdrawalStatus.SUCCESS
                } catch (e: TransactionException) {
                    savedWithdrawal.status = WithdrawalStatus.FAILED
                } catch (e: Exception) {
                    savedWithdrawal.status = WithdrawalStatus.INTERNAL_ERROR
                } finally {
                    notifyStatus(savedWithdrawal)
                }
            }
        }
        return savedWithdrawal
    }

    override fun findAll(): List<WithdrawalAsap> = withdrawalRepository.findAll()

    private fun notifyStatus(savedWithdrawal: WithdrawalAsap) {
        notificationService.send(savedWithdrawal)
        withdrawalRepository.save(savedWithdrawal)
    }

}