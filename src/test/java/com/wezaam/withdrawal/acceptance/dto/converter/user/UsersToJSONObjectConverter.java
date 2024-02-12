package com.wezaam.withdrawal.acceptance.dto.converter.user;

import com.wezaam.withdrawal.acceptance.dto.Users;
import com.wezaam.withdrawal.acceptance.dto.converter.JSONConverter;
import org.json.JSONObject;

import java.util.function.Function;

public class UsersToJSONObjectConverter implements Function<Users, JSONObject> {
    @Override
    public JSONObject apply(Users users) {
        return JSONConverter.toJSON(users);
    }
}
