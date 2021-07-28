package com.wezaam.withdrawal.acceptance.dto.converter.user;

import com.wezaam.withdrawal.acceptance.dto.User;
import com.wezaam.withdrawal.acceptance.dto.converter.JSONConverter;
import org.json.JSONObject;

import java.util.function.Function;

public class JSONObjectToUserConverter  implements Function<JSONObject, User> {
    @Override
    public User apply(JSONObject user) {
        return JSONConverter.fromJSON(user, User.class);
    }
}
