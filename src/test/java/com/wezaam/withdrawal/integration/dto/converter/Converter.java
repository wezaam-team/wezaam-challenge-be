package com.wezaam.withdrawal.integration.dto.converter;

public interface Converter<K, V> {

    V from(K source);

    K to(V source);
}
