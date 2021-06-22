package com.wezaam.withdrawal.model;

import javax.persistence.*

@Entity(name = "users")
data class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id:Long,
    val firstName:String,
    @OneToMany(mappedBy="user")
    val paymentMethods: List<PaymentMethod>,
    val maxWithdrawalAmount:Double
)