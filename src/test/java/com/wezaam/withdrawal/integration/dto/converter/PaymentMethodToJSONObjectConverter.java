package com.wezaam.withdrawal.integration.dto.converter;

import com.wezaam.withdrawal.integration.dto.PaymentMethod;
import org.json.JSONObject;

import java.util.function.Function;

public class PaymentMethodToJSONObjectConverter implements Function<PaymentMethod, JSONObject> {
    @Override
    public JSONObject apply(PaymentMethod paymentMethod) {
        return JSONConverter.toJSON(paymentMethod);
    }
}
