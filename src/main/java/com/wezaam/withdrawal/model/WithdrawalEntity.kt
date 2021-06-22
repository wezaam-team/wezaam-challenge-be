package com.wezaam.withdrawal.model

import java.time.Instant
import javax.persistence.*

@Entity(name = "withdrawals")
data class WithdrawalEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id:Long,
        val transactionId:Long,
        val amount:Double,
        val createdAt: Instant,
        val userId:Long,
        @ManyToOne
        val paymentMethod:PaymentMethod,
        @Enumerated(EnumType.STRING)
        val status:WithdrawalStatus
)
