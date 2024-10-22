package leonbet.com.parser;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Created by Joanna Pakosh, on 10/2024
 */

public class DateTimeUtil {

	public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss z";
	public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

	public static LocalDateTime parseToLocalDateTime(Long startDateTime) {
		Instant instant = Instant.ofEpochMilli(startDateTime);
		LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));
		return ldt;
	}

	public static String convertLocaDateTimeToUTCString(LocalDateTime ldt) {
		return Objects.isNull(ldt) ? "" : DATE_TIME_FORMATTER.format(ZonedDateTime.of(ldt, ZoneId.of("UTC")));
	}
}
