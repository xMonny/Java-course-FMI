package bg.sofia.uni.fmi.mjt.foodanalyzer.server.command;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.storage.FoodStorageAccessor;

import java.util.Objects;

import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.Message.NULL_FOOD_ACCESSOR_MESSAGE;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.Message.NULL_SEARCH_KEY_MESSAGE;

public abstract class Command {

    protected final String key;
    protected final boolean extraNutrientIncluded;

    protected FoodStorageAccessor foodStorageAccessor;

    public Command(FoodStorageAccessor accessor, String key, boolean extraNutrientIncluded) {
        if (accessor == null) {
            throw new IllegalArgumentException(NULL_FOOD_ACCESSOR_MESSAGE);
        }
        if (key == null) {
            throw new IllegalArgumentException(NULL_SEARCH_KEY_MESSAGE);
        }
        this.foodStorageAccessor = accessor;
        this.key = key;
        this.extraNutrientIncluded = extraNutrientIncluded;
    }

    public abstract String getResponse();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Command command = (Command) o;
        return extraNutrientIncluded == command.extraNutrientIncluded
                && key.equals(command.key)
                && foodStorageAccessor.equals(command.foodStorageAccessor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, extraNutrientIncluded, foodStorageAccessor);
    }

    protected boolean notMatching(String value, String pattern) {
        return value == null || !value.matches(pattern);
    }
}
