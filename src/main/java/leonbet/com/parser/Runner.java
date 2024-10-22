package leonbet.com.parser;

/**
* Created by Joanna Pakosh, on 10/2024
*/

public record Runner (Long id, String name, double indicator) {

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(name).append(", ").append(indicator).append(", ").append(id);
		return builder.toString();
	}
}
