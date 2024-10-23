package leonbet.com.parser;

import static leonbet.com.parser.Constants.ID;
import static leonbet.com.parser.Constants.LEAGUES;
import static leonbet.com.parser.Constants.MAX_COUNT_THREADS;
import static leonbet.com.parser.Constants.NAME;
import static leonbet.com.parser.Constants.NAME_DEFAULT;
import static leonbet.com.parser.Constants.REGIONS;
import static leonbet.com.parser.Constants.SPORTS;
import static leonbet.com.parser.Constants.TOP;
import static leonbet.com.parser.Constants.TOP_ORDER;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Joanna Pakosh, on 10/2024
 */

public class BookmakerParserApp {

	public static void main(String[] args) throws InterruptedException, IOException, ExecutionException {
		ExecutorService executorService = Executors.newFixedThreadPool(MAX_COUNT_THREADS);
		List<Callable<Sport>> tasks = new ArrayList<>();
		
		List<Sport> sports = new ArrayList<>();
		executorService.submit(() -> {
			CompletableFuture<String> mainResponse = HttpUtil.getRequestAndResponse(HttpUtil.getMainUrl());
			String basicResponse = null;
			try {
				basicResponse = mainResponse.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			
			JSONArray sportMainList = new JSONArray(basicResponse);
			for (int i = 0; i < sportMainList.length(); i++) {
				JSONObject sportJson = sportMainList.getJSONObject(i);
				String sportName = sportJson.getString(NAME);
				if (SPORTS.contains(sportName)) {
					Sport sport = new Sport(sportName, new ArrayList<>());
					List<League> leaguesList = new ArrayList<>();
					JSONArray leagues = sportJson.getJSONArray(REGIONS);
					for (int j = 0; j < leagues.length(); j++) {
						JSONArray leagueArray = leagues.getJSONObject(j).getJSONArray(LEAGUES);
						for (int k = 0; k < leagueArray.length(); k++) {
							JSONObject leagueJson = leagueArray.getJSONObject(k);
							Integer topOrder = leagueJson.getInt(TOP_ORDER);
							Boolean isTop = leagueJson.getBoolean(TOP);
							if (isTop) {
								Long id = leagueJson.getLong(ID);
								String name = leagueJson.getString(NAME);
								String nameDefault = leagues.getJSONObject(j).getString(NAME_DEFAULT);
								League league = new League(id, name, nameDefault, topOrder, new ArrayList<>());
								leaguesList.add(league);
							}
						}					
					}
					Collections.sort(leaguesList);
					sport.leagues().addAll(leaguesList);
					sports.add(sport);
				}
			}
		}).get();
		
		for(Sport sport: sports) {
			AsyncTaskParse asyncTaskParse = new AsyncTaskParse(sport);
			tasks.add(asyncTaskParse);
		}

		List<Future<Sport>> futureData = executorService.invokeAll(tasks);

		for (Future<Sport> future : futureData) {
			// print all required data
			Sport sport = future.get();
			sport.leagues().forEach(league -> {
				System.out.print(sport);
				System.out.println(league);

				league.matches().forEach(match -> {
					System.out.println(String.format("%5s %s", "", match));
					match.allMarketsData().keySet().forEach(key -> {
						System.out.println(String.format("%10s %s", "", key));
						match.allMarketsData().get(key).forEach(value -> {
							System.out.println(String.format("%15s %s", "", value));
						});
					});
				});
			});
		}
		executorService.shutdown();
	}
}
