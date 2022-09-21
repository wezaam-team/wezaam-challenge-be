package com.wezaam.withdrawal.cashe

import com.wezaam.withdrawal.model.Withdrawal
import java.util.concurrent.ConcurrentHashMap
import org.springframework.stereotype.Component

@Component
class OnMemoryFailedNotificationsCache : FailedNotificationCache {

    private val notifications: ConcurrentHashMap<String, Withdrawal> = ConcurrentHashMap()

    override fun get(key: String): Withdrawal? {
        return notifications[key]
    }

    override fun set(key: String, withdrawal: Withdrawal) {
        notifications[key] = withdrawal
    }

    override fun remove(key: String) {
        notifications.remove(key)
    }

    override fun getValues(): MutableCollection<Withdrawal> = notifications.values

    override fun getAll(): ConcurrentHashMap<String, Withdrawal> = notifications


}