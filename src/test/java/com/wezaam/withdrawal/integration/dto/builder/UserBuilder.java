package com.wezaam.withdrawal.integration.dto.builder;

import com.wezaam.withdrawal.integration.dto.PaymentMethod;
import com.wezaam.withdrawal.integration.dto.User;

public class UserBuilder {
    private String name;
    private Long id;
    private PaymentMethod paymentMethod;

    public static UserBuilder aUserBuilder() {
        return new UserBuilder();
    }

    public UserBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public UserBuilder withPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public User build() {
        return new User(name, id, paymentMethod);
    }
}