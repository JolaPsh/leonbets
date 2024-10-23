package leonbet.com.parser;

import java.util.List;

/**
* Created by Joanna Pakosh, on 10/2024
*/

public record Sport (String name, List<League> leagues) {	

	@Override
	public String toString() {
		return name + ", ";
	}
}