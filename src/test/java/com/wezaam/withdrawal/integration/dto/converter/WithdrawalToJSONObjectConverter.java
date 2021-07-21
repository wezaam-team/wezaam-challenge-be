package com.wezaam.withdrawal.integration.dto.converter;

import com.wezaam.withdrawal.integration.dto.Withdrawal;
import org.json.JSONObject;

import java.util.function.Function;

public class WithdrawalToJSONObjectConverter implements Function<Withdrawal, JSONObject> {
    @Override
    public JSONObject apply(Withdrawal withdrawal) {
        return JSONConverter.toJSON(withdrawal);
    }
}
