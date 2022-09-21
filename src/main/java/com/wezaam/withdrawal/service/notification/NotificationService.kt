package com.wezaam.withdrawal.service.notification

import com.wezaam.withdrawal.model.Withdrawal

interface NotificationService {
    fun send(withdrawal: Withdrawal)
    fun resend(key: String, withdrawal: Withdrawal)
}