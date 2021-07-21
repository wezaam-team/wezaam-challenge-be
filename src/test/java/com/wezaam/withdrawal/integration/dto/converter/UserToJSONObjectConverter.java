package com.wezaam.withdrawal.integration.dto.converter;

import com.wezaam.withdrawal.integration.dto.User;
import org.json.JSONObject;

import java.util.function.Function;

public class UserToJSONObjectConverter implements Function<User, JSONObject> {
    @Override
    public JSONObject apply(User user) {
        return JSONConverter.toJSON(user);
    }
}
