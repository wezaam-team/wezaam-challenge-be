package com.wezaam.withdrawal.acceptance.dto.converter;

import com.wezaam.withdrawal.acceptance.dto.Withdrawal;
import com.wezaam.withdrawal.acceptance.dto.converter.withdrawal.JSONObjectToWithdrawalConverter;
import com.wezaam.withdrawal.acceptance.dto.converter.withdrawal.WithdrawalToJSONObjectConverter;
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
