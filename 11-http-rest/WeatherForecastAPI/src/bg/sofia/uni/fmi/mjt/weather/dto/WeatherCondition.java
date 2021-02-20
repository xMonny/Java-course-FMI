package bg.sofia.uni.fmi.mjt.weather.dto;

import java.util.Objects;

public class WeatherCondition {

    private final String description;

    public WeatherCondition(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WeatherCondition that = (WeatherCondition) o;
        return description.equals(that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }
}
