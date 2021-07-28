package com.wezaam.withdrawal.acceptance.dto;

import java.util.List;

public class User {

    private Long id;

    private String name;

    private List<Long> paymentIds;

    public User() {
        this(null, null, null);
    }

    public User(Long id, String name, List<Long> paymentIds) {
        this.id = id;
        this.name = name;
        this.paymentIds = paymentIds;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Long> getPaymentIds() {
        return paymentIds;
    }
}
