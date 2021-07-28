package com.wezaam.withdrawal.acceptance.dto.converter;

public interface Converter<K, V> {

    V from(K source);

    K to(V source);
}
