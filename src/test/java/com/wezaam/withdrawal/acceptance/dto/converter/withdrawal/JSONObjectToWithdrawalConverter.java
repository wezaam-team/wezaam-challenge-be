package com.wezaam.withdrawal.acceptance.dto.converter.withdrawal;

import com.wezaam.withdrawal.acceptance.dto.Withdrawal;
import com.wezaam.withdrawal.acceptance.dto.converter.JSONConverter;
import org.json.JSONObject;

import java.util.function.Function;

public class JSONObjectToWithdrawalConverter implements Function<JSONObject, Withdrawal> {
    @Override
    public Withdrawal apply(JSONObject withdrawal) {
        return JSONConverter.fromJSON(withdrawal, Withdrawal.class);
    }
}
