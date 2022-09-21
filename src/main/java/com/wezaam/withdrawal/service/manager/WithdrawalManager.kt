package com.wezaam.withdrawal.service.manager

import com.wezaam.withdrawal.dto.CreateWithdrawalRequest
import com.wezaam.withdrawal.enums.WithdrawalExecutionMethod
import com.wezaam.withdrawal.map.WithdrawalMapper
import com.wezaam.withdrawal.model.WithdrawalAsap
import com.wezaam.withdrawal.model.WithdrawalScheduled
import com.wezaam.withdrawal.service.WithdrawalService
import com.wezaam.withdrawal.validation.CreateWithdrawalRequestValidator
import org.springframework.stereotype.Component

@Component
class WithdrawalManager(
    private val createWithdrawalRequestValidators: List<CreateWithdrawalRequestValidator>,
    private val withdrawalAsapService: WithdrawalService<WithdrawalAsap>,
    private val withdrawalScheduledService: WithdrawalService<WithdrawalScheduled>,
    private val withdrawalMapper: WithdrawalMapper
) {

    fun create(createWithdrawalRequest: CreateWithdrawalRequest) {
        createWithdrawalRequestValidators.forEach { it.validate(createWithdrawalRequest) }

        when (createWithdrawalRequest.withdrawalExecutionMethod) {
            WithdrawalExecutionMethod.ASAP -> withdrawalAsapService.create(withdrawalMapper.toWithdrawal(createWithdrawalRequest))
            WithdrawalExecutionMethod.SCHEDULED -> withdrawalScheduledService.create(withdrawalMapper.toScheduledWithdrawal(createWithdrawalRequest))
        }
    }

    fun findAllWithdrawals(): MutableList<Any> {
        val result: MutableList<Any> = ArrayList()

        withdrawalAsapService.findAll().let { result.addAll(it) }
        withdrawalScheduledService.findAll().let { result.addAll(it) }

        return result
    }
}