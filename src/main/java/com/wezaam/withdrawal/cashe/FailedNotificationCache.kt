package com.wezaam.withdrawal.cashe

import com.wezaam.withdrawal.model.Withdrawal
import java.util.concurrent.ConcurrentHashMap

interface FailedNotificationCache {
    fun get(key: String): Withdrawal?
    fun set(key: String, withdrawal: Withdrawal)
    fun remove(key: String)
    fun getValues(): MutableCollection<Withdrawal>
    fun getAll(): ConcurrentHashMap<String, Withdrawal>
}