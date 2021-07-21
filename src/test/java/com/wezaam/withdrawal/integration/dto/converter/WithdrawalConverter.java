package com.wezaam.withdrawal.integration.dto.converter;

import com.wezaam.withdrawal.integration.dto.Withdrawal;
import org.json.JSONObject;

public class WithdrawalConverter implements Converter<Withdrawal, JSONObject> {

    private static final WithdrawalToJSONObjectConverter TO_JSON_CONVERTER = new WithdrawalToJSONObjectConverter();
    private static final JSONObjectToWithdrawalConverter FROM_JSON_CONVERTER = new JSONObjectToWithdrawalConverter();

    @Override
    public JSONObject from(Withdrawal source) {
        return TO_JSON_CONVERTER.apply(source);
    }

    @Override
    public Withdrawal to(JSONObject source) {
        return FROM_JSON_CONVERTER.apply(source);
    }
}
