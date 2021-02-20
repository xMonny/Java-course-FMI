package bg.sofia.uni.fmi.mjt.foodanalyzer.server.handler;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.Food;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.type.FoodType;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.handler.exception.request.FoodException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.handler.exception.request.FoodNotFoundException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.handler.parse.JSONParser;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public final class RequestHandler {

    private static final String API_KEY = "92M0MCdmd0uZ0nSciyuCMFyzXqXLtEGO5PFlizEP";

    private static final String SCHEME = "https";
    private static final String HOST = "api.nal.usda.gov";

    private static final String PATH_SEARCH = "/fdc/v1/search";
    private static final String PATH_REPORT = "/fdc/v1/food/%d";

    private static final String SEARCH_QUERY = "query=%s&requireAllWords=true&api_key=%s";
    private static final String REPORT_QUERY = "api_key=%s";

    private static final String NULL_CLIENT_MESSAGE = "Detected null http client";
    private static final String NOT_FOUND_FOOD_MESSAGE = "Food not found";
    private static final String NOT_EXTRACTED_FOOD_MESSAGE = "Food cannot be extracted";
    private static final String EXECUTION_EXCEPTION_MESSAGE = "Attempting to retrieve result of aborted task: ";
    private static final String INTERRUPTED_EXCEPTION_MESSAGE = "Retrieving status code for request was interrupted: ";

    private static final String URI_SYNTAX_EXCEPTION_MESSAGE = "Error in parsing to URI reference: ";

    private final HttpClient client;

    public RequestHandler(HttpClient client) {
        if (client == null) {
            throw new IllegalArgumentException(NULL_CLIENT_MESSAGE);
        }
        this.client = client;
    }

    private int getStatusCode(CompletableFuture<HttpResponse<String>> asyncResponse) {
        if (asyncResponse == null) {
            return 0;
        }
        try {
            return asyncResponse.get().statusCode();
        } catch (InterruptedException e) {
            System.err.println(INTERRUPTED_EXCEPTION_MESSAGE + e.getMessage());
            e.printStackTrace();
            return 0;
        } catch (ExecutionException e) {
            System.err.println(EXECUTION_EXCEPTION_MESSAGE + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    private CompletableFuture<String> getResponse(URI uri) throws FoodException {

        HttpRequest request = HttpRequest.newBuilder().uri(uri).build();

        CompletableFuture<HttpResponse<String>> asyncResponse = client
                .sendAsync(request, HttpResponse.BodyHandlers.ofString());

        int statusCode = getStatusCode(asyncResponse);
        if (statusCode == 404) {
            throw new FoodNotFoundException(NOT_FOUND_FOOD_MESSAGE);
        } else if (statusCode != 200) {
            throw new FoodException(NOT_EXTRACTED_FOOD_MESSAGE);
        }

        return asyncResponse
                .thenApply(HttpResponse::body);
    }

    private String extractJsonString(URI uri) throws FoodException {
        CompletableFuture<String> response = getResponse(uri);
        if (response == null) {
            return null;
        }
        return response.join();
    }

    public Food handleSearchRequest(String searchInput) throws FoodException {
        try {
            URI uri = new URI(SCHEME, HOST, PATH_SEARCH,
                    SEARCH_QUERY.formatted(searchInput, API_KEY), null);

            String jsonString = extractJsonString(uri);
            if (jsonString == null) {
                return null;
            }

            return JSONParser.parseToFood(jsonString, FoodType.SEARCH);

        } catch (URISyntaxException e) {
            System.err.println(URI_SYNTAX_EXCEPTION_MESSAGE + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Food handleReportRequest(int fdcId) throws FoodException {
        try {
            URI uri = new URI(SCHEME, HOST, PATH_REPORT.formatted(fdcId), REPORT_QUERY.formatted(API_KEY), null);

            String jsonString = extractJsonString(uri);
            if (jsonString == null) {
                return null;
            }

            return JSONParser.parseToFood(jsonString, FoodType.REPORT);

        } catch (URISyntaxException e) {
            System.err.println(URI_SYNTAX_EXCEPTION_MESSAGE + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
