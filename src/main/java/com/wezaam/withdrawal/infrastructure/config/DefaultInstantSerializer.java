package com.wezaam.withdrawal.infrastructure.config;

import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;

public class DefaultInstantSerializer extends InstantSerializer {

    public DefaultInstantSerializer() {
        super();
    }
}
