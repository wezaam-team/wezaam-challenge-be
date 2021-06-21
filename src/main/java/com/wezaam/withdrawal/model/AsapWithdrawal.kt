package com.wezaam.withdrawal.model

import com.wezaam.withdrawal.dto.WithdrawalInformationDTO
import com.wezaam.withdrawal.exception.TransactionException
import com.wezaam.withdrawal.repository.WithdrawalRepository
import org.springframework.stereotype.Component
import java.util.stream.Collectors

@Component
class AsapWithdrawal(
        private val repository: WithdrawalRepository
) : Withdrawal {

    override fun persistWithdrawal(withdrawal:WithdrawalInformationDTO): WithdrawalInformationDTO {
        try {
            val withdrawalPersisted = repository.save(WithdrawalEntity(
                    withdrawal.id, withdrawal.transactionId,
                    withdrawal.amount,withdrawal.createdAt,
                    withdrawal.userId,withdrawal.paymentMethod,
                    withdrawal.status
            ))

            return WithdrawalInformationDTO(
                    withdrawalPersisted.id,
                    withdrawalPersisted.transactionId,
                    withdrawalPersisted.amount,
                    withdrawalPersisted.createdAt,
                    null,
                    withdrawalPersisted.userId,
                    withdrawalPersisted.paymentMethod,
                    withdrawalPersisted.status,
                    WithdrawType.ASAP)
        } catch (exception:Exception){
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
                    null,
                    withdrawalPersisted.userId,
                    withdrawalPersisted.paymentMethod,
                    withdrawalPersisted.status,
                    WithdrawType.ASAP)}
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
                        null,
                        withdrawalPersisted.userId,
                        withdrawalPersisted.paymentMethod,
                        withdrawalPersisted.status,
                        WithdrawType.ASAP)}
                .collect(Collectors.toList())
    }
}
