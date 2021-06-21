package com.wezaam.withdrawal.service

import com.wezaam.withdrawal.dto.WithdrawalInformationDTO
import com.wezaam.withdrawal.exception.TransactionException
import com.wezaam.withdrawal.model.WithdrawalStatus
import org.springframework.stereotype.Service

@Service
class WithdrawalService(val factory:WithdrawalFactory,
                        private val processService:WithdrawalProcessingService,
                        private val notifierService:EventsService
                        ) {
    private var transactionId:Long = 0

    /**
     * This method creates a withdrawal since withdrawal type
     */
    fun create(informationDTO: WithdrawalInformationDTO): WithdrawalInformationDTO{
        val withdrawal = factory.create(informationDTO)
        return withdrawal.persistWithdrawal(informationDTO)
    }

    /**
     * Get IdTransaction and update status
     */
    fun process(informationDTO: WithdrawalInformationDTO): WithdrawalInformationDTO{
        try {
            transactionId = processService.sendToProcessing(informationDTO.amount, informationDTO.paymentMethod)
            val updatedWithdrawal = create(WithdrawalInformationDTO(
                    informationDTO.id,
                    transactionId,
                    informationDTO.amount,
                    informationDTO.createdAt,
                    informationDTO.executeAt,
                    informationDTO.userId,
                    informationDTO.paymentMethod,
                    WithdrawalStatus.PROCESSING,
                    informationDTO.type))
            notifierService.send(updatedWithdrawal)
            return updatedWithdrawal
        } catch (e:Exception) {
                if (e is TransactionException) {
                    return create(WithdrawalInformationDTO(
                            informationDTO.id,
                            transactionId,
                            informationDTO.amount,
                            informationDTO.createdAt,
                            informationDTO.executeAt,
                            informationDTO.userId,
                            informationDTO.paymentMethod,
                            WithdrawalStatus.FAILED,
                            informationDTO.type))
                } else {
                    return create(WithdrawalInformationDTO(
                            informationDTO.id,
                            transactionId,
                            informationDTO.amount,
                            informationDTO.createdAt,
                            informationDTO.executeAt,
                            informationDTO.userId,
                            informationDTO.paymentMethod,
                            WithdrawalStatus.INTERNAL_ERROR,
                            informationDTO.type))
                }
            }
    }


    fun findAllWithdrawals(): MutableList<WithdrawalInformationDTO> {
        return factory.findAllWithdrawal()
    }
}