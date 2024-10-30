package com.wezaam.withdrawal.application.representation;

import com.wezaam.withdrawal.domain.model.PaymentMethod;

public class PaymentMethodRepresentation {

    private Long id;
    private String name;

    public PaymentMethodRepresentation(PaymentMethod paymentMethod) {
        this.setId(paymentMethod.getId());
        this.setName(paymentMethod.getName());
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
