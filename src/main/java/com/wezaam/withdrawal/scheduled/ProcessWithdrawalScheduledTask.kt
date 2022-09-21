package com.wezaam.withdrawal.scheduled

import com.wezaam.withdrawal.service.WithdrawalScheduledService
import java.time.Instant
import java.util.concurrent.CompletableFuture.runAsync
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ProcessWithdrawalScheduledTask(
    private val withdrawalScheduledService: WithdrawalScheduledService
) {
    @Scheduled(fixedDelay = 5000)
    fun run() {
        withdrawalScheduledService.findAllByExecuteAtBefore(Instant.now()).forEach {
            runAsync { withdrawalScheduledService.process(it) }
        }
    }
}