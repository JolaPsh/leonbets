package leonbet.com.parser;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
* Created by Joanna Pakosh, on 10/2024
*/

public final class Constants {

	static final String ID = "id";
	static final String NAME = "name";
	static final String NAME_DEFAULT = "nameDefault";
	static final String REGIONS = "regions";
	static final String LEAGUES = "leagues";
	static final String TOP_ORDER = "topOrder";
	static final String TOP = "top";
	static final String DATA = "data";
	static final String KICKOFF = "kickoff";
	static final String MARKETS = "markets";
	static final String RUNNERS = "runners";
	static final String PRICE_STR = "priceStr";

	static final Set<String> SPORTS = new HashSet<>(Arrays.asList("Football", "Tennis", "Ice Hockey", "Basketball"));

	static final String BASE_URL = "https://leon11.bet/api-2/betline/";
	static final String JSON_VALUE = "application/json";
	static final String ACCEPT_HEADER = "Accept";
	
	static final String LEAGUE_ID = "league_id=";
	static final String EVENT_ID = "eventId=";
	static final String CTAG_LANGUAGE = "ctag=en-US";
	static final String FLAG_URLV2 = "flags=urlv2";
	static final String FLAGS = "flags=reg,urlv2,mm2,rrc,nodup,smg,outv2";
	static final String VTAG_NUM = "vtag=9c2cd386-31e1-4ce9-a140-28e9b63a9300";
	static final String AMPERSAND = "&";
	static final String HIDE_CLOSED = "hideClosed=true";

	static final int MAX_COUNT_THREADS = 3;
	static final int MAX_COUNT_MATCHES = 2;
}
