package leonbet.com.parser;

import static leonbet.com.parser.Constants.DATA;
import static leonbet.com.parser.Constants.ID;
import static leonbet.com.parser.Constants.KICKOFF;
import static leonbet.com.parser.Constants.MARKETS;
import static leonbet.com.parser.Constants.MAX_COUNT_MATCHES;
import static leonbet.com.parser.Constants.NAME;
import static leonbet.com.parser.Constants.PRICE_STR;
import static leonbet.com.parser.Constants.RUNNERS;
import static leonbet.com.parser.HttpUtil.getAllMarketsDataUrl;
import static leonbet.com.parser.HttpUtil.getMatchesUrl;
import static leonbet.com.parser.HttpUtil.getRequestAndResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Joanna Pakosh, on 10/2024
 */

public class AsyncTaskParse implements Callable<Sport> {

	private final Sport sport;

	public AsyncTaskParse(Sport sport) {
		this.sport = sport;
	}

	@Override
	public Sport call() throws Exception {
		for (League league : sport.leagues()) {
			Long leagueId = league.id();
			league.matches().addAll(parseFirstTwoMatches(leagueId));
		}
		return sport;
	}

	private List<Match> parseFirstTwoMatches(Long leagueId) throws JSONException, InterruptedException, ExecutionException {
		CompletableFuture<String> matchesResponse = getRequestAndResponse(getMatchesUrl(leagueId.toString()));
		List<Match> matches = new ArrayList<>();
		JSONObject parseMatches = new JSONObject(matchesResponse.get());
		JSONArray matchesList = parseMatches.getJSONArray(DATA);

		int matchesLength = matchesList.length() < MAX_COUNT_MATCHES? matchesList.length() : MAX_COUNT_MATCHES;
		for (int i = 0; i < matchesLength; i++) {
			JSONObject matchJson = matchesList.getJSONObject(i);
			Long eventId = matchJson.getLong(ID);
			Map<String, List<Runner>> allMarketsData = parseAllMarketsData(eventId);
			String name = matchJson.getString(NAME);
			LocalDateTime startDateTime = DateTimeUtil.parseToLocalDateTime(matchJson.getLong(KICKOFF));

			Match match = new Match(eventId, name, startDateTime, allMarketsData);
			matches.add(match);
		}
		return matches;
	}

	private Map<String, List<Runner>> parseAllMarketsData(Long eventId) throws JSONException, InterruptedException, ExecutionException {
		CompletableFuture<String> allMarketsDataResponse = getRequestAndResponse(getAllMarketsDataUrl(eventId.toString()));
		JSONObject parsedAllMarketsData = new JSONObject(allMarketsDataResponse.get());
		JSONArray marketDataList = parsedAllMarketsData.getJSONArray(MARKETS);

		Map<String, List<Runner>> allMarketsData = new HashMap<>();
		for (int i = 0; i < marketDataList.length(); i++) {
			JSONObject marketJson = marketDataList.getJSONObject(i);
			String marketName = marketJson.getString(NAME);
			JSONArray runnersList = marketJson.getJSONArray(RUNNERS);

			List<Runner> runnerList = new LinkedList<>();
			for (int j = 0; j < runnersList.length(); j++) {
				JSONObject runnerJson = runnersList.getJSONObject(j);
				Runner runnerObj = new Runner(runnerJson.getLong(ID), runnerJson.getString(NAME), runnerJson.getDouble(PRICE_STR));
				runnerList.add(runnerObj);
			}
			allMarketsData.put(marketName, runnerList);
		}
		return allMarketsData;
	}
}
