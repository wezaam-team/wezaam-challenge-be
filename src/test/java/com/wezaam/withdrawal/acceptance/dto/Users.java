package com.wezaam.withdrawal.acceptance.dto;

import java.util.List;

public class Users {

    List<User> users;

    public Users() {
        this(null);
    }

    public Users(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
