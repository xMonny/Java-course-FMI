package bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public final class Criteria implements Serializable {

    @SerializedName("query")
    private final String name;

    public Criteria(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Criteria that = (Criteria) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
