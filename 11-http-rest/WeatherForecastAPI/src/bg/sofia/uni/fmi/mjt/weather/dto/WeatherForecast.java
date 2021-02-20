package bg.sofia.uni.fmi.mjt.weather.dto;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.Objects;

public class WeatherForecast {

    @SerializedName("weather")
    private final WeatherCondition[] condition;

    @SerializedName("main")
    private final WeatherData data;

    public WeatherForecast(WeatherCondition[] condition, WeatherData data) {
        this.condition = condition;
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WeatherForecast that = (WeatherForecast) o;
        return Arrays.equals(condition, that.condition)
                && data.equals(that.data);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(data);
        result = 31 * result + Arrays.hashCode(condition);
        return result;
    }
}
