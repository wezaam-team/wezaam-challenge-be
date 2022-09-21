package com.wezaam.withdrawal.service.notification

import com.wezaam.withdrawal.cashe.FailedNotificationCache
import com.wezaam.withdrawal.exception.ProviderException
import com.wezaam.withdrawal.model.Withdrawal
import com.wezaam.withdrawal.util.StringGenerator
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class InConsoleNotificationMockedService(private val failedNotificationCache: FailedNotificationCache) : NotificationService {

    val mockedNotificationFailure = true
    val mockedNotificationResendFailure = false
    val keyLength = 10

    @Async
    override fun send(withdrawal: Withdrawal) {
        try {
            if (mockedNotificationFailure) throw ProviderException("The Notification Could not be delivered")
            println("--------------- Notifying status: ${withdrawal.status} of the withdrawal: $withdrawal")
        } catch (e: ProviderException) {
            println("--------------- Provider Failure for withdrawal: $withdrawal")
            val key = StringGenerator.generate(keyLength)
            failedNotificationCache.set(key, withdrawal)
            println("--------------- Saving withdrawal in cache -> key:  $key withdrawal: $withdrawal")
        }
    }

    @Async
    override fun resend(key: String, withdrawal: Withdrawal) {
        try {
            if (mockedNotificationResendFailure) throw ProviderException("The Notification Could not be delivered")
            println("--------------- Resent key -> $key Notifying status: ${withdrawal.status} of the withdrawal: $withdrawal")
            failedNotificationCache.remove(key)
        } catch (e: ProviderException) {
            println("The Notification Could not be delivered")
        }
    }

}