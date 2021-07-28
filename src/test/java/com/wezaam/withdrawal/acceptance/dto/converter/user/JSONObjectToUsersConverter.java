package com.wezaam.withdrawal.acceptance.dto.converter.user;

import com.wezaam.withdrawal.acceptance.dto.Users;
import com.wezaam.withdrawal.acceptance.dto.converter.JSONConverter;
import org.json.JSONObject;

import java.util.function.Function;

public class JSONObjectToUsersConverter implements Function<JSONObject, Users> {
    @Override
    public Users apply(JSONObject users) {
        return JSONConverter.fromJSON(users, Users.class);
    }
}
