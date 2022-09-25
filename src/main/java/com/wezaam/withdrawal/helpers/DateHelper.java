package com.wezaam.withdrawal.helpers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateHelper {

    public static ZonedDateTime toZonedDateTime(LocalDateTime localDateTime) {
        return localDateTime == null ? null : ZonedDateTime.of(localDateTime, ZoneId.of("Z"));
    }
}
