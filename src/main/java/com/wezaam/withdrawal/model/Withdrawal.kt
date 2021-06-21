package com.wezaam.withdrawal.model

import com.wezaam.withdrawal.dto.WithdrawalInformationDTO


interface Withdrawal {
    fun persistWithdrawal(withdrawal:WithdrawalInformationDTO) : WithdrawalInformationDTO
    fun findAllWithdrawal() : List<WithdrawalInformationDTO>
    fun findAllFailedWithdrawal() : List<WithdrawalInformationDTO>
}