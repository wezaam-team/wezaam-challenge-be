package com.wezaam.withdrawal.integration.dto.converter;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONConverter {

    final static ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    public static JSONObject toJSON(Object source) {
        JSONObject sourceAsJSON;

        try {
            String sourceAsString = MAPPER.writeValueAsString(source);
            sourceAsJSON = new JSONObject(sourceAsString);
        } catch (JsonProcessingException | JSONException e) {
            sourceAsJSON = new JSONObject();
        }

        return sourceAsJSON;
    }

    public static <T> T fromJSON(JSONObject source, Class<T> targetClass) {
        T target;

        try {
            target = MAPPER.readValue(source.toString(), targetClass);
        } catch (JsonProcessingException e) {
            target = null;
        }

        return target;
    }
}
