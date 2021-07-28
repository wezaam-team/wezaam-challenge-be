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
}
