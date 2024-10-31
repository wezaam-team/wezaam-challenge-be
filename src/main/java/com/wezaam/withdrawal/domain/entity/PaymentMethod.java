package com.wezaam.withdrawal.domain.entity;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethod {
    private Long id;
    private String name;
}
