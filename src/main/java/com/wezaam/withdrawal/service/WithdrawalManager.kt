package com.wezaam.withdrawal.service

import com.wezaam.withdrawal.dto.CreateWithdrawalRequest
import com.wezaam.withdrawal.enums.WithdrawalExecutionMethod
import com.wezaam.withdrawal.map.WithdrawalMapper
import com.wezaam.withdrawal.validation.WithdrawalValidator
import org.springframework.stereotype.Component

@Component
class WithdrawalManager(
    private val withdrawalValidator: WithdrawalValidator,
    private val withdrawalAsapService: WithdrawalAsapService,
    private val withdrawalScheduledService: WithdrawalScheduledService,
    private val withdrawalMapper: WithdrawalMapper
) {

    fun create(createWithdrawalRequest: CreateWithdrawalRequest) {
        withdrawalValidator.validate(createWithdrawalRequest)

        when (createWithdrawalRequest.withdrawalExecutionMethod) {
            WithdrawalExecutionMethod.ASAP -> withdrawalAsapService.create(withdrawalMapper.mapToWithdrawal(createWithdrawalRequest))
            WithdrawalExecutionMethod.SCHEDULED -> withdrawalScheduledService.save(withdrawalMapper.mapToScheduledWithdrawal(createWithdrawalRequest))
        }
    }

    fun findAllWithdrawals(): MutableList<Any> {
        val result: MutableList<Any> = ArrayList()
        result.add(withdrawalAsapService.findAll())
        result.add(withdrawalScheduledService.findAll())
        return result
    }
}