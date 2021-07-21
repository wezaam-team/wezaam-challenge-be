package com.wezaam.withdrawal.integration.dto;

import java.io.Serializable;

public class User implements Serializable {
    public User(String name, Long id, PaymentMethod paymentMethod) {
        this.name = name;
        this.id = id;
        this.paymentMethod = paymentMethod;
    }

    private String name;
    private Long id;
    private PaymentMethod paymentMethod;
}
