package com.wezaam.withdrawal.integration.dto.converter;

import com.wezaam.withdrawal.integration.dto.PaymentMethod;
import org.json.JSONObject;

public class PaymentMethodConverter implements Converter<PaymentMethod, JSONObject> {

    private static final PaymentMethodToJSONObjectConverter TO_JSON_CONVERTER = new PaymentMethodToJSONObjectConverter();
    private static final JSONObjectToPaymentMethodConverter FROM_JSON_CONVERTER = new JSONObjectToPaymentMethodConverter();

    @Override
    public JSONObject from(PaymentMethod source) {
        return TO_JSON_CONVERTER.apply(source);
    }

    @Override
    public PaymentMethod to(JSONObject source) {
        return FROM_JSON_CONVERTER.apply(source);
    }
}
