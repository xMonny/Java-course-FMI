package bg.sofia.uni.fmi.mjt.weather.exceptions;

public class WeatherForecastClientException extends Exception {

    public WeatherForecastClientException(String message) {
        super(message);
    }

    public WeatherForecastClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
