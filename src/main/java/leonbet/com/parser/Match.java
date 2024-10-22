package leonbet.com.parser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Created by Joanna Pakosh, on 10/2024
 */

public record Match(Long eventId, String name, LocalDateTime startDateTime, Map<String, List<Runner>> allMarketsData) {

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(name).append(", ").append(DateTimeUtil.convertLocaDateTimeToUTCString(startDateTime))
				.append(", ").append(eventId);
		return builder.toString();
	}
}
