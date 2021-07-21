package com.wezaam.withdrawal.integration.dto.converter;

import com.wezaam.withdrawal.integration.dto.User;
import org.json.JSONObject;

public class UserConverter implements Converter<User, JSONObject> {

    private static final UserToJSONObjectConverter TO_JSON_CONVERTER = new UserToJSONObjectConverter();
    private static final JSONObjectToUserConverter FROM_JSON_CONVERTER = new JSONObjectToUserConverter();

    @Override
    public JSONObject from(User source) {
        return TO_JSON_CONVERTER.apply(source);
    }

    @Override
    public User to(JSONObject source) {
        return FROM_JSON_CONVERTER.apply(source);
    }
}