package com.wezaam.withdrawal.service

interface WithdrawalService<Withdrawal> {
    fun create(withdrawal: Withdrawal) : Withdrawal
    fun findAll(): List<Withdrawal>
}