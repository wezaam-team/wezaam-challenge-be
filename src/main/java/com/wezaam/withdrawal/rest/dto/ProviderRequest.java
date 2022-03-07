package com.wezaam.withdrawal.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProviderRequest {

    private String identifier;

    private BigDecimal amount;

    private String currency;


}
