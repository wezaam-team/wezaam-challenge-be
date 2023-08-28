package com.wezaam.withdrawal.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@MappedSuperclass
public abstract class Withdrawal {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	private Long transactionId;
	private Double amount;
	private Instant createdAt;
	private Long userId;
	private Long paymentMethodId;
	@Enumerated(EnumType.STRING)
	private WithdrawalStatus status;

}
