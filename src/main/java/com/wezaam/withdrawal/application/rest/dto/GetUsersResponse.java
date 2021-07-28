package com.wezaam.withdrawal.application.rest.dto;

import java.util.List;

public class GetUsersResponse {

    List<GetUserResponse> users;

    public GetUsersResponse(List<GetUserResponse> users) {
        this.users = users;
    }

    public List<GetUserResponse> getUsers() {
        return users;
    }

    public void setUsers(List<GetUserResponse> users) {
        this.users = users;
    }
}
