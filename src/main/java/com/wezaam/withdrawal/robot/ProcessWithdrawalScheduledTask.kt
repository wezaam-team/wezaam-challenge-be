package com.wezaam.withdrawal.robot

import com.wezaam.withdrawal.service.ProcessWithdrawalScheduledService
import com.wezaam.withdrawal.service.WithdrawalScheduledService
import java.time.Instant
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ProcessWithdrawalScheduledTask(
    private val withdrawalScheduledService: WithdrawalScheduledService,
    private val processWithdrawalScheduledService: ProcessWithdrawalScheduledService
) {

    @Scheduled(fixedDelay = 5000)
    fun run() {
        withdrawalScheduledService.findAllByExecuteAtBefore(Instant.now()).forEach { processWithdrawalScheduledService.process(it) }
    }
}