package com.wezaam.withdrawal.application.rest.dto;

import java.util.List;

public class GetUserResponse {
    private Long id;

    private String name;

    private List<Long> paymentIds;

    protected GetUserResponse(Long id, String name, List<Long> paymentIds) {
        this.id = id;
        this.name = name;
        this.paymentIds = paymentIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getPaymentIds() {
        return paymentIds;
    }

    public void setPaymentIds(List<Long> paymentIds) {
        this.paymentIds = paymentIds;
    }
}
