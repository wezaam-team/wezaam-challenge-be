package com.wezaam.withdrawal.model;

import com.wezaam.withdrawal.model.enums.WithdrawalStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.Instant;

import static javax.persistence.GenerationType.IDENTITY;

@Setter
@Getter
@Entity(name = "withdrawals")
@EqualsAndHashCode
@ToString
public class Withdrawal {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private Instant createdAt;
    private Instant executeAt;
    private Long transactionId;
    private Double amount;
    private Long userId;
    private Long paymentMethodId;
    @Enumerated(EnumType.STRING)
    private WithdrawalStatus status;
}
