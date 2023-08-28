package com.wezaam.withdrawal.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Utils {
	public static Instant parseStringTimeToInstant(String dateInstant) {
		return LocalDateTime.parse(
						dateInstant,
						DateTimeFormatter.ISO_LOCAL_DATE_TIME
				)
				.toInstant(ZoneOffset.UTC);
	}
}
