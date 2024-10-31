package com.wezaam.withdrawal.infrastructure.rest.handler;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProblemDetails {

    private String code;
    private String message;
}
