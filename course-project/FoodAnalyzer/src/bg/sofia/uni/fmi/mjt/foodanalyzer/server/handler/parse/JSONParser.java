package bg.sofia.uni.fmi.mjt.foodanalyzer.server.handler.parse;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.Food;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.FoodReport;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.FoodSearch;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.type.FoodType;
import com.google.gson.Gson;

public class JSONParser {

    private static final String NULL_JSON_STRING = "Detected null json string in input";
    private static final String NULL_FOOD_TYPE = "Detected null food type in input";

    public static Food parseToFood(String jsonString, FoodType foodType) {
        if (jsonString == null) {
            throw new IllegalArgumentException(NULL_JSON_STRING);
        }
        if (foodType == null) {
            throw new IllegalArgumentException(NULL_FOOD_TYPE);
        }
        Gson gson = new Gson();
        switch (foodType) {
            case SEARCH -> {
                return gson.fromJson(jsonString, FoodSearch.class);
            }
            case REPORT -> {
                return gson.fromJson(jsonString, FoodReport.class);
            }
            default -> {
                return null;
            }
        }
    }
}
