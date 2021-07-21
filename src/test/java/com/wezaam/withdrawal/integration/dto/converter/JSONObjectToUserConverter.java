package com.wezaam.withdrawal.integration.dto.converter;

import com.wezaam.withdrawal.integration.dto.User;
import org.json.JSONObject;

import java.util.function.Function;

public class JSONObjectToUserConverter implements Function<JSONObject, User> {
    @Override
    public User apply(JSONObject user) {
        return JSONConverter.fromJSON(user, User.class);
    }
}
