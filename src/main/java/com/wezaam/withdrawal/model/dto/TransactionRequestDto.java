package com.wezaam.withdrawal.model.dto;

import com.wezaam.withdrawal.model.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TransactionRequestDto {

	private Double amount;
	private PaymentMethod paymentMethod;
	private Long transactionId;
}
