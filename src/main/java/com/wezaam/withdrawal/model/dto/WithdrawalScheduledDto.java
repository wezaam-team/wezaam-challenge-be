package com.wezaam.withdrawal.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class WithdrawalScheduledDto {

	private Long userId;
	private Long paymentMethodId;
	private Double amount;
	private Instant executeAt;

	public void setExecuteAt (String value) {
		this.executeAt = value.equals("ASAP") ? Instant.now() : null;
	}
}
