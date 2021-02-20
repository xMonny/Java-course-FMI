package bg.sofia.uni.fmi.mjt.weather.dto;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class WeatherData {

    private final double temp;

    @SerializedName("feels_like")
    private final double feels;

    public WeatherData(double temp, double feelsLike) {
        this.temp = temp;
        this.feels = feelsLike;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WeatherData data = (WeatherData) o;
        return Double.compare(temp, data.temp) == 0
                && Double.compare(feels, data.feels) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(temp, feels);
    }
}
