package bg.sofia.uni.fmi.mjt.foodanalyzer.server.command;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.Food;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.handler.exception.request.FoodException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.storage.FoodStorageAccessor;

import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.KeyPattern.FDC_ID_PATTERN;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.Message.FOOD_PROBLEM_OCCURRED_MESSAGE;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.Message.INVALID_FOOD_FDC_ID_MESSAGE;

public final class ReceiveFoodByReportCommand extends Command {

    public ReceiveFoodByReportCommand(FoodStorageAccessor accessor, String key,
                                      boolean extraNutrientIncluded) {
        super(accessor, key, extraNutrientIncluded);
    }

    @Override
    public String getResponse() {
        if (notMatching(key, FDC_ID_PATTERN)) {
            return INVALID_FOOD_FDC_ID_MESSAGE;
        } else {
            try {
                int keyInt = Integer.parseInt(key);
                Food foodByReport = foodStorageAccessor.extractFoodByReport(keyInt);
                return foodByReport.toString(extraNutrientIncluded);
            } catch (FoodException e) {
                return FOOD_PROBLEM_OCCURRED_MESSAGE + e.getMessage();
            }
        }
    }
}
