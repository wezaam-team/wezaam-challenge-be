package com.wezaam.withdrawal.service

import com.wezaam.withdrawal.dto.WithdrawalInformationDTO
import com.wezaam.withdrawal.model.*
import com.wezaam.withdrawal.repository.WithdrawalRepository
import com.wezaam.withdrawal.repository.WithdrawalScheduledRepository
import org.springframework.stereotype.Component

@Component
open class WithdrawalFactory (private val withdrawalRepository:WithdrawalRepository,
                         private val withdrawalScheduleRepository:WithdrawalScheduledRepository) {

    fun create(newWithdrawal:WithdrawalInformationDTO):Withdrawal{
        return when (newWithdrawal.type){
            WithdrawType.ASAP -> AsapWithdrawal(withdrawalRepository)
            WithdrawType.SCHEDULE -> ScheduleWithdrawal(withdrawalScheduleRepository)
        }
    }

    fun findAllWithdrawal():MutableList<WithdrawalInformationDTO>{
        val withdrawalList:MutableList<WithdrawalInformationDTO> = AsapWithdrawal(withdrawalRepository).findAllWithdrawal()
        withdrawalList.addAll(ScheduleWithdrawal(withdrawalScheduleRepository).findAllWithdrawal())
        return withdrawalList
    }

    fun findAllPendingWithdrawal():MutableList<WithdrawalInformationDTO>{
        return ScheduleWithdrawal(withdrawalScheduleRepository).findAllPendingWithdrawal()
    }

    fun findAllFailedWithdrawal():MutableList<WithdrawalInformationDTO>{
        val withdrawalList:MutableList<WithdrawalInformationDTO> = AsapWithdrawal(withdrawalRepository).findAllFailedWithdrawal()
        withdrawalList.addAll(ScheduleWithdrawal(withdrawalScheduleRepository).findAllFailedWithdrawal())
        return withdrawalList
    }
}