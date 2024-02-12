package com.wezaam.withdrawal.acceptance.dto.converter;

import com.wezaam.withdrawal.acceptance.dto.Users;
import com.wezaam.withdrawal.acceptance.dto.converter.user.JSONObjectToUsersConverter;
import com.wezaam.withdrawal.acceptance.dto.converter.user.UsersToJSONObjectConverter;
import org.json.JSONObject;

public class UsersConverter implements Converter<Users, JSONObject> {

    private static final UsersToJSONObjectConverter TO_JSON_CONVERTER = new UsersToJSONObjectConverter();
    private static final JSONObjectToUsersConverter FROM_JSON_CONVERTER = new JSONObjectToUsersConverter();

    @Override
    public JSONObject from(Users source) {
        return TO_JSON_CONVERTER.apply(source);
    }

    @Override
    public Users to(JSONObject source) {
        return FROM_JSON_CONVERTER.apply(source);
    }
}
