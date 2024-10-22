package leonbet.com.parser;

import java.util.List;

/**
 * Created by Joanna Pakosh, on 10/2024
 */

public record League(Long id, String name, String nameDefault, int topOrder, List<Match> matches)
		implements Comparable<League> {

	@Override
	public int compareTo(League o) {
		return Integer.compare(this.topOrder, o.topOrder);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder().append(nameDefault).append(" ").append(name);
		return builder.toString();
	}
}
