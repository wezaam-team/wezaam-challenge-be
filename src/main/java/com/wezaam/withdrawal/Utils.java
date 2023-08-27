package com.wezaam.withdrawal;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Utils {
	public static Instant parseStringTimeToInstant(String dateInstant) {
		return LocalDateTime.parse(
						dateInstant,
//						2023-08-27T21:12:40.585Z
//						DateTimeFormatter.ofPattern("yyyy-MM-ddTHH:mm:ss.SSS", Locale.getDefault())
// 						'2011-12-03T10:15:30'
						DateTimeFormatter.ISO_LOCAL_DATE_TIME
				)
				.toInstant(ZoneOffset.UTC);
	}
}
