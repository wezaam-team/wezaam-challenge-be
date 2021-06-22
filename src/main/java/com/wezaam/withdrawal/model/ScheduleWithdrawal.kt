package com.wezaam.withdrawal.model

import com.wezaam.withdrawal.dto.WithdrawalInformationDTO
import com.wezaam.withdrawal.exception.TransactionException
import com.wezaam.withdrawal.repository.WithdrawalScheduledRepository
import java.time.Instant
import java.util.stream.Collectors

data class ScheduleWithdrawal(private val repository: WithdrawalScheduledRepository) : Withdrawal {
    override fun persistWithdrawal(withdrawal: WithdrawalInformationDTO): WithdrawalInformationDTO {
        try {
            val withdrawalPersisted = repository.save(WithdrawalScheduled(
                    withdrawal.id,
                    withdrawal.transactionId,
                    withdrawal.amount,
                    withdrawal.createdAt,
                    withdrawal.executeAt!!,
                    withdrawal.userId,
                    withdrawal.paymentMethod,
                    withdrawal.status
            ))
            return WithdrawalInformationDTO(
                    withdrawalPersisted.id,
                    withdrawalPersisted.transactionId,
                    withdrawalPersisted.amount,
                    withdrawalPersisted.createdAt,
                    withdrawalPersisted.executeAt,
                    withdrawalPersisted.userId,
                    withdrawalPersisted.paymentMethod,
                    withdrawalPersisted.status,
                    WithdrawType.SCHEDULE)
        }
        catch (exception:Exception){
        throw TransactionException("Error trying to persist data in DB")
    }
    }

    override fun findAllWithdrawal(): MutableList<WithdrawalInformationDTO> {
        val withdrawalList = repository.findAll()
        return withdrawalList.stream()
                .map{
                    withdrawalPersisted ->  WithdrawalInformationDTO(
                        withdrawalPersisted.id,
                        withdrawalPersisted.transactionId,
                        withdrawalPersisted.amount,
                        withdrawalPersisted.createdAt,
                        withdrawalPersisted.executeAt,
                        withdrawalPersisted.userId,
                        withdrawalPersisted.paymentMethod,
                        withdrawalPersisted.status,
                        WithdrawType.SCHEDULE)}
                .collect(Collectors.toList())
    }

    override fun findAllFailedWithdrawal(): MutableList<WithdrawalInformationDTO> {
        val withdrawalList = repository.findAllByFailedStatus()
        return withdrawalList.stream()
                .map{
                    withdrawalPersisted ->  WithdrawalInformationDTO(
                        withdrawalPersisted.id,
                        withdrawalPersisted.transactionId,
                        withdrawalPersisted.amount,
                        withdrawalPersisted.createdAt,
                        withdrawalPersisted.executeAt,
                        withdrawalPersisted.userId,
                        withdrawalPersisted.paymentMethod,
                        withdrawalPersisted.status,
                        WithdrawType.SCHEDULE)}
                .collect(Collectors.toList())
    }

    fun findAllPendingWithdrawal():MutableList<WithdrawalInformationDTO>{
        val withdrawalList = repository.findAllByExecuteAtBefore(Instant.now())
        return withdrawalList.stream()
                .map{
                    withdrawalPersisted ->  WithdrawalInformationDTO(
                        withdrawalPersisted.id,
                        withdrawalPersisted.transactionId,
                        withdrawalPersisted.amount,
                        withdrawalPersisted.createdAt,
                        withdrawalPersisted.executeAt,
                        withdrawalPersisted.userId,
                        withdrawalPersisted.paymentMethod,
                        withdrawalPersisted.status,
                        WithdrawType.SCHEDULE)}
                .collect(Collectors.toList())
    }
}
