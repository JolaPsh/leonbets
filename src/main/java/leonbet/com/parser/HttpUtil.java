package leonbet.com.parser;

import static leonbet.com.parser.Constants.ACCEPT_HEADER;
import static leonbet.com.parser.Constants.AMPERSAND;
import static leonbet.com.parser.Constants.BASE_URL;
import static leonbet.com.parser.Constants.CTAG_LANGUAGE;
import static leonbet.com.parser.Constants.EVENT_ID;
import static leonbet.com.parser.Constants.FLAGS;
import static leonbet.com.parser.Constants.FLAG_URLV2;
import static leonbet.com.parser.Constants.HIDE_CLOSED;
import static leonbet.com.parser.Constants.JSON_VALUE;
import static leonbet.com.parser.Constants.LEAGUE_ID;
import static leonbet.com.parser.Constants.VTAG_NUM;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

/**
 * Created by Joanna Pakosh, on 10/2024
 */

public class HttpUtil {

	static final String getMainUrl() {
		StringBuilder mainUrlBuilder = new StringBuilder(BASE_URL);
		mainUrlBuilder.append("sports?").append(CTAG_LANGUAGE).append(AMPERSAND).append(FLAG_URLV2);
		return mainUrlBuilder.toString();
	}

	static final String getMatchesUrl(String leagueId) {
		StringBuilder matchesUrlBuilder = new StringBuilder(BASE_URL);
		matchesUrlBuilder.append("changes/all?").append(CTAG_LANGUAGE).append(AMPERSAND).append(VTAG_NUM)
				.append(AMPERSAND).append(LEAGUE_ID).append(leagueId).append(AMPERSAND).append(HIDE_CLOSED);
		return matchesUrlBuilder.toString();
	}

	static final String getAllMarketsDataUrl(String eventId) {
		StringBuilder allMarketsDataUrlBuilder = new StringBuilder(BASE_URL);
		allMarketsDataUrlBuilder.append("event/all?").append(CTAG_LANGUAGE).append(AMPERSAND).append(EVENT_ID)
				.append(eventId).append(AMPERSAND).append(FLAGS);
		return allMarketsDataUrlBuilder.toString();
	}

	static HttpRequest buildGetRequest(String url) {
		return HttpRequest.newBuilder().uri(URI.create(url)).header(ACCEPT_HEADER, JSON_VALUE).GET().build();
	}

	static CompletableFuture<String> getHttpResponse(HttpRequest request) {
		HttpClient client = HttpClient.newHttpClient();
		CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,
				HttpResponse.BodyHandlers.ofString());
		return response.thenApplyAsync(HttpResponse::body);
	}

	static CompletableFuture<String> getRequestAndResponse(String url) {
		HttpRequest paramRequest = HttpUtil.buildGetRequest(url);
		return HttpUtil.getHttpResponse(paramRequest);
	}
}
