package com.wezaam.withdrawal.application.rest.dto;

import java.util.List;

public class GetUserResponseBuilder {
    private Long id;
    private String name;
    private List<Long> paymentIds;

    private GetUserResponseBuilder() {
        super();
    }

    public static GetUserResponseBuilder aGetUsersResponseBuilder() {
        return new GetUserResponseBuilder();
    }

    public GetUserResponseBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public GetUserResponseBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public GetUserResponseBuilder withPaymentIds(List<Long> paymentIds) {
        this.paymentIds = paymentIds;
        return this;
    }

    public GetUserResponse build() {
        return new GetUserResponse(id, name, paymentIds);
    }
}