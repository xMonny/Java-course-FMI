package bg.sofia.uni.fmi.mjt.weather;

import bg.sofia.uni.fmi.mjt.weather.dto.WeatherCondition;
import bg.sofia.uni.fmi.mjt.weather.dto.WeatherData;
import bg.sofia.uni.fmi.mjt.weather.dto.WeatherForecast;
import bg.sofia.uni.fmi.mjt.weather.exceptions.LocationNotFoundException;
import bg.sofia.uni.fmi.mjt.weather.exceptions.WeatherForecastClientException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.ArgumentMatchers;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WeatherForecastClientTest {

    @Mock
    private HttpClient httpClientMock;

    @Mock
    private HttpResponse<String> httpResponseMock;

    @Test(expected = IllegalArgumentException.class)
    public void testWeatherForecastWhenHttpClientIsNull() {
        new WeatherForecastClient(null);
    }

    @Test(expected = WeatherForecastClientException.class)
    public void testGetForecastWhenCityIsNull() throws WeatherForecastClientException {
        WeatherForecastClient client = new WeatherForecastClient(HttpClient.newBuilder().build());
        client.getForecast(null);
    }

    @Test(expected = WeatherForecastClientException.class)
    public void testGetForecastWhenResponseIsNull() throws WeatherForecastClientException {
        WeatherForecastClient client = new WeatherForecastClient(HttpClient.newBuilder().build());
        client.getForecast(null);
    }

    @Test(expected = LocationNotFoundException.class)
    public void testGetForecastWhenCodeIs404() throws Exception {
        WeatherForecastClient client = new WeatherForecastClient(httpClientMock);
        when(httpClientMock.send(Mockito.any(HttpRequest.class),
                ArgumentMatchers.<HttpResponse.BodyHandler<String>>any())).thenReturn(httpResponseMock);
        when(httpResponseMock.statusCode()).thenReturn(404);
        client.getForecast("pleven");
    }

    @Test(expected = WeatherForecastClientException.class)
    public void testGetForecastWhenFailed() throws Exception {
        WeatherForecastClient client = new WeatherForecastClient(httpClientMock);
        when(httpClientMock.send(Mockito.any(HttpRequest.class),
                ArgumentMatchers.<HttpResponse.BodyHandler<String>>any())).thenReturn(httpResponseMock);
        when(httpResponseMock.statusCode()).thenReturn(401);
        client.getForecast("pleven");
    }

    @Test
    public void testGetForecastForPlovdiv() throws Exception {
        String jsonString = "{\"coord\":{\"lon\":24.75,\"lat\":42.15}"
                + ",\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"sunny\",\"icon\":\"04d\"}]"
                + ",\"base\":\"stations\",\"main\":{\"temp\":2.09,\"feels_like\":-6.55,\"temp_min\":2,\"temp_max\":2.22"
                + ",\"pressure\":1012,\"humidity\":48},\"visibility\":10000,\"wind\":{\"speed\":8.23,\"deg\":280}"
                + ",\"clouds\":{\"all\":90},\"dt\":1610796674,\"sys\":{\"type\":1,\"id\":6363,\"country\":\"BG\""
                + ",\"sunrise\":1610775983,\"sunset\":1610810083},\"timezone\":7200,\"id\":728193,\"name\":\"Plovdiv\""
                + ",\"cod\":200}";

        WeatherForecastClient client = new WeatherForecastClient(httpClientMock);

        when(httpClientMock.send(Mockito.any(HttpRequest.class),
                ArgumentMatchers.<HttpResponse.BodyHandler<String>>any())).thenReturn(httpResponseMock);
        when(httpResponseMock.statusCode()).thenReturn(200);

        when(httpResponseMock.body()).thenReturn(jsonString);
        WeatherForecast expected = new WeatherForecast(new WeatherCondition[] {new WeatherCondition("sunny")},
                new WeatherData(2.09, -6.55));
        WeatherForecast actual = client.getForecast("");

        assertEquals(expected, actual);
    }

    @Test
    public void testGetForecastForPleven() throws Exception {
        String jsonString = "{\"coord\":{\"lon\":24.6167,\"lat\":43.4167}"
                + ",\"weather\":[{\"id\":741,\"main\":\"Fog\",\"description\":\"fog\",\"icon\":\"50n\"}]"
                + ",\"base\":\"stations\",\"main\":{\"temp\":2,\"feels_like\":-1.77"
                + ",\"temp_min\":2,\"temp_max\":2,\"pressure\":1020,\"humidity\":93}"
                + ",\"visibility\":100,\"wind\":{\"speed\":2.77,\"deg\":36},\"clouds\":{\"all\":90},\"dt\":1610400798"
                + ",\"sys\":{\"type\":1,\"id\":6369,\"country\":\"BG\",\"sunrise\":1610344369,\"sunset\":1610377541}"
                + ",\"timezone\":7200,\"id\":728203,\"name\":\"Pleven\",\"cod\":200}";

        WeatherForecastClient client = new WeatherForecastClient(httpClientMock);

        when(httpClientMock.send(Mockito.any(HttpRequest.class),
                ArgumentMatchers.<HttpResponse.BodyHandler<String>>any())).thenReturn(httpResponseMock);
        when(httpResponseMock.statusCode()).thenReturn(200);

        when(httpResponseMock.body()).thenReturn(jsonString);
        WeatherForecast expected = new WeatherForecast(new WeatherCondition[] {new WeatherCondition("fog")},
                new WeatherData(2.0, -1.77));
        WeatherForecast actual = client.getForecast("Pleven");

        assertEquals(expected, actual);
    }
}
