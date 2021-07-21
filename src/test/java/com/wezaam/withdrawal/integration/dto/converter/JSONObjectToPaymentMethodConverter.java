package com.wezaam.withdrawal.integration.dto.converter;

import com.wezaam.withdrawal.integration.dto.PaymentMethod;
import org.json.JSONObject;

import java.util.function.Function;

public class JSONObjectToPaymentMethodConverter implements Function<JSONObject, PaymentMethod> {
    @Override
    public PaymentMethod apply(JSONObject paymentMethod) {
        return JSONConverter.fromJSON(paymentMethod, PaymentMethod.class);
    }
}
