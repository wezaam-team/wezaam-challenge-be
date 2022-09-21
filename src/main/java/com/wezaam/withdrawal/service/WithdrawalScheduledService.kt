package com.wezaam.withdrawal.service

import com.wezaam.withdrawal.model.WithdrawalScheduled
import com.wezaam.withdrawal.repository.WithdrawalScheduledRepository
import java.time.Instant
import org.springframework.stereotype.Service

@Service
class WithdrawalScheduledService(private val withdrawalScheduledRepository: WithdrawalScheduledRepository)  {
    fun findAll(): MutableList<WithdrawalScheduled> = withdrawalScheduledRepository.findAll()
    fun findAllByExecuteAtBefore(instant: Instant): MutableList<WithdrawalScheduled> = withdrawalScheduledRepository.findAllByExecuteAtBefore(instant)
    fun save(withdrawalScheduled: WithdrawalScheduled) = withdrawalScheduledRepository.save(withdrawalScheduled)
}