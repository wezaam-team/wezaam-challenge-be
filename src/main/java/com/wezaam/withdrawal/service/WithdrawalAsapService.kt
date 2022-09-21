package com.wezaam.withdrawal.service

import com.wezaam.withdrawal.exception.TransactionException
import com.wezaam.withdrawal.model.PaymentMethod
import com.wezaam.withdrawal.model.Withdrawal
import com.wezaam.withdrawal.model.WithdrawalStatus
import com.wezaam.withdrawal.repository.PaymentMethodRepository
import com.wezaam.withdrawal.repository.WithdrawalRepository
import java.util.concurrent.Executors
import org.springframework.stereotype.Service

@Service
class WithdrawalAsapService (
    private val withdrawalRepository: WithdrawalRepository,
    private val withdrawalProcessingService: WithdrawalProcessingService,
    private val paymentMethodRepository: PaymentMethodRepository,
    private val eventsService: EventsService
)  {

    private val executorService = Executors.newCachedThreadPool()

    fun create(withdrawal: Withdrawal) {
        val pendingWithdrawal = withdrawalRepository.save(withdrawal)
        executorService.submit {
            val savedWithdrawalOptional = withdrawalRepository.findById(pendingWithdrawal.id)
            val paymentMethod: PaymentMethod? = if (savedWithdrawalOptional.isPresent) {
                paymentMethodRepository.findById(savedWithdrawalOptional.get().paymentMethodId).orElse(null)
            } else {
                null
            }
            if (savedWithdrawalOptional.isPresent && paymentMethod != null) {
                val savedWithdrawal = savedWithdrawalOptional.get()
                try {
                    savedWithdrawal.status = WithdrawalStatus.PROCESSING
                    savedWithdrawal.transactionId = withdrawalProcessingService.sendToProcessing(withdrawal.amount, paymentMethod)
                } catch (e: TransactionException) {
                    savedWithdrawal.status = WithdrawalStatus.FAILED
                } catch (e: Exception) {
                    savedWithdrawal.status = WithdrawalStatus.INTERNAL_ERROR
                } finally {
                    withdrawalRepository.save(savedWithdrawal)
                    eventsService.send(savedWithdrawal)
                }
            }
        }
    }

     fun findAll(): MutableList<Withdrawal> = withdrawalRepository.findAll()
}