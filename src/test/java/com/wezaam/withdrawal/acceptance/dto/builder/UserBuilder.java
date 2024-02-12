package com.wezaam.withdrawal.acceptance.dto.builder;

import com.wezaam.withdrawal.acceptance.dto.User;

import java.util.List;

public class UserBuilder {
    private Long id;
    private String name;
    private List<Long> paymentIds;

    private UserBuilder() {
        super();
    }

    public static UserBuilder aUserBuilder() {
        return new UserBuilder();
    }

    public UserBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public UserBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder withPaymentIds(List<Long> paymentIds) {
        this.paymentIds = paymentIds;
        return this;
    }

    public User build() {
        return new User(id, name, paymentIds);
    }
}