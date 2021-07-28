package com.wezaam.withdrawal.acceptance.dto.converter.user;

import com.wezaam.withdrawal.acceptance.dto.User;
import com.wezaam.withdrawal.acceptance.dto.converter.JSONConverter;
import org.json.JSONObject;

import java.util.function.Function;

public class UserToJSONObjectConverter implements Function<User, JSONObject> {
    @Override
    public JSONObject apply(User user) {
        return JSONConverter.toJSON(user);
    }
}
