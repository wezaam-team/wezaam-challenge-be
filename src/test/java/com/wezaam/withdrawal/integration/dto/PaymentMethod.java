package com.wezaam.withdrawal.integration.dto;

import java.io.Serializable;

public class PaymentMethod implements Serializable {
    private String name;

    public PaymentMethod(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    private Long id;
}
