package com.weezam.challenge.withdrawal.adapter.out.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "withdrawals")
public class WithdrawalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_id")
    private Long transactionId;

    private Double amount;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "execute_at")
    private Instant executeAt;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "payment_method_id")
    private Long paymentMethodId;

    private String status;
}
