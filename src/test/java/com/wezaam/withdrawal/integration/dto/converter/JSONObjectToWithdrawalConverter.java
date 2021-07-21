package com.wezaam.withdrawal.integration.dto.converter;

import com.wezaam.withdrawal.integration.dto.Withdrawal;
import org.json.JSONObject;

import java.util.function.Function;

public class JSONObjectToWithdrawalConverter implements Function<JSONObject, Withdrawal> {
    @Override
    public Withdrawal apply(JSONObject withdrawal) {
        return JSONConverter.fromJSON(withdrawal, Withdrawal.class);
    }
}
