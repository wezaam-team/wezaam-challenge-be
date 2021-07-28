package com.wezaam.withdrawal.acceptance.dto.builder;

import com.wezaam.withdrawal.acceptance.dto.User;
import com.wezaam.withdrawal.acceptance.dto.Users;

import java.util.List;

public class UsersBuilder {

    private List<User> users;

    private UsersBuilder() {
        super();
    }

    public static UsersBuilder aUsersBuilder() {
        return new UsersBuilder();
    }

    public UsersBuilder withUsers(List<User> users) {
        this.users = users;
        return this;
    }

    public Users build() {
        return new Users(users);
    }

}
