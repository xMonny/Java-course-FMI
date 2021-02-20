package bg.sofia.uni.fmi.mjt.weather;

import bg.sofia.uni.fmi.mjt.weather.dto.WeatherForecast;
import bg.sofia.uni.fmi.mjt.weather.exceptions.WeatherForecastClientException;
import bg.sofia.uni.fmi.mjt.weather.exceptions.LocationNotFoundException;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WeatherForecastClient {

    private static final String API_KEY = "<KEY>";

    private static final String SCHEME = "http";
    private static final String HOST = "api.openweathermap.org";
    private static final String PATH = "/data/2.5/weather";

    private final HttpClient weatherHttpClient;

    public WeatherForecastClient(HttpClient weatherHttpClient) {

        if (weatherHttpClient == null) {
            throw new IllegalArgumentException("Http client is null");
        }
        this.weatherHttpClient = weatherHttpClient;
    }

    private URI getWeatherURI(String city) throws WeatherForecastClientException {

        try {
            String query = "q=" + city + "&units=metric&lang=english&appid=" + API_KEY;
            return new URI(SCHEME, HOST, PATH, query, null);

        } catch (Exception e) {
            throw new WeatherForecastClientException(e.getMessage(), e);
        }
    }

    private String getResponseFor(String city) throws WeatherForecastClientException {

        try {
            URI uri = getWeatherURI(city);

            HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
            HttpResponse<String> httpResponse = weatherHttpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (httpResponse.statusCode() == 404) {
                throw new LocationNotFoundException("City is not found");
            }
            if (httpResponse.statusCode() != 200) {
                throw new WeatherForecastClientException("Cannot extract weather forecast for city " + city);
            }

            return httpResponse.body();

        } catch (IOException | InterruptedException e) {
            throw new WeatherForecastClientException(e.getMessage(), e);
        }
    }

    /**
     * Fetches the weather forecast for the specified city.
     *
     * @return the forecast
     * @throws LocationNotFoundException if the city is not found
     * @throws WeatherForecastClientException if information regarding the weather
     * for this location could not be retrieved
     */
    public WeatherForecast getForecast(String city) throws WeatherForecastClientException {

        String response = getResponseFor(city);
        Gson gson = new Gson();
        return gson.fromJson(response, WeatherForecast.class);
    }
}
