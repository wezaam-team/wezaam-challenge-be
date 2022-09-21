package com.wezaam.withdrawal.scheduled

import com.wezaam.withdrawal.cashe.FailedNotificationCache
import com.wezaam.withdrawal.service.notification.InConsoleNotificationMockedService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class FailedNotificationScheduledTask(
    private val inConsoleNotificationMockedService: InConsoleNotificationMockedService,
    private val failedNotificationCache: FailedNotificationCache
) {

    @Scheduled(fixedDelay = 10000)
    fun run() {
        failedNotificationCache.getAll().forEach { (key, value) -> inConsoleNotificationMockedService.resend(key, value) }
    }
}