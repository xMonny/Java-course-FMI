package bg.sofia.uni.fmi.mjt.foodanalyzer.server.command;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.Food;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.handler.exception.request.FoodException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.storage.FoodStorageAccessor;

import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.KeyPattern.FOOD_NAME_PATTERN;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.Message.FOOD_PROBLEM_OCCURRED_MESSAGE;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.Message.FOUND_ZERO_FOODS_MESSAGE;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.Message.INVALID_FOOD_NAME_MESSAGE;

public final class ReceiveFoodByNameCommand extends Command {

    public ReceiveFoodByNameCommand(FoodStorageAccessor accessor, String key,
                                    boolean extraNutrientIncluded) {
        super(accessor, key, extraNutrientIncluded);
    }

    @Override
    public String getResponse() {
        if (notMatching(key, FOOD_NAME_PATTERN)) {
            return INVALID_FOOD_NAME_MESSAGE;
        } else {
            try {
                Food foodByName = foodStorageAccessor.extractFoodByName(key);
                if (foodByName.getTotal() == 0) {
                    return FOUND_ZERO_FOODS_MESSAGE;
                }
                return foodByName.toString(extraNutrientIncluded);
            } catch (FoodException e) {
                return FOOD_PROBLEM_OCCURRED_MESSAGE + e.getMessage();
            }
        }
    }
}
