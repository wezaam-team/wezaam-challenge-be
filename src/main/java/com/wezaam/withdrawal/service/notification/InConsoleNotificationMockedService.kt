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

    //TODO apply separately for scheduled and asap with an interface
    @Async
    override fun send(withdrawal: Withdrawal) {
        try {
            if (mockedNotificationFailure) throw ProviderException("The Notification Could not be delivered")
            println("--------------- Notifying status: ${withdrawal.status} of the withdrawal: $withdrawal")
        } catch (e: ProviderException) {
            println("--------------- Provider Failure for withdrawal: $withdrawal")
            val key = StringGenerator.generate(keyLength)

            failedNotificationCache.set(key, clone(withdrawal))
            println("--------------- Saving withdrawal in cache -> key:  $key withdrawal: $withdrawal")
        }
    }

    //TODO this not here
    private fun clone(withdrawal: Withdrawal): Withdrawal {
        val withdrawalSave = Withdrawal()
        withdrawalSave.id = withdrawal.id
        withdrawalSave.amount = withdrawal.amount
        withdrawalSave.status = withdrawal.status
        withdrawalSave.paymentMethodId = withdrawal.paymentMethodId
        withdrawalSave.createdAt = withdrawal.createdAt
        withdrawalSave.transactionId = withdrawal.transactionId
        withdrawalSave.userId = withdrawal.userId
        return withdrawalSave
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