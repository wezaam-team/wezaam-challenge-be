package com.wezaam.withdrawal.integration.dto.builder;

import com.wezaam.withdrawal.integration.dto.PaymentMethod;

public class PaymentMethodBuilder {
    private String name;
    private Long id;

    public static PaymentMethodBuilder aPaymentMethodBuilder() {
        return new PaymentMethodBuilder();
    }

    public PaymentMethodBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public PaymentMethodBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public PaymentMethod build() {
        return new PaymentMethod(name, id);
    }
}