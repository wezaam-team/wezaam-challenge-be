package com.wezaam.withdrawal.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class WithdrawalScheduledDto {

	@NotNull
	private Long userId;
	@NotNull
	private Long paymentMethodId;
	@NotNull
	private Double amount;
	@NotNull
	private String executeAt;

}
