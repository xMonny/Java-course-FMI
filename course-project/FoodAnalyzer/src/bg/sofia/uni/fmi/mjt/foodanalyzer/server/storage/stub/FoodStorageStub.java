package bg.sofia.uni.fmi.mjt.foodanalyzer.server.storage.stub;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.Food;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.FoodSearch;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.type.CriteriaType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class FoodStorageStub {
    private static final Map<String, Food> nameFoods = new ConcurrentHashMap<>();
    private static final Map<String, Food> gtinUpcFoods = new ConcurrentHashMap<>();
    private static final Map<String, Food> foodCodeFoods = new ConcurrentHashMap<>();
    private static final Map<Integer, Food> fdcIdFoods = new ConcurrentHashMap<>();

    private static final FoodStorageStub FOOD_STORAGE_STUB = new FoodStorageStub();

    private FoodStorageStub() {
    }

    static FoodStorageStub getStorage() {
        return FOOD_STORAGE_STUB;
    }

    private static void distribute(Food food) {
        food.getSearchFoods().stream()
                .filter(f -> f.getGtinUpc() != null)
                .forEach(f -> {
                    FoodSearch foodSearch = new FoodSearch(f, CriteriaType.BARCODE);
                    gtinUpcFoods.putIfAbsent(foodSearch.getGtinUpc(), foodSearch);
                });
        food.getSearchFoods().stream()
                .filter(f -> f.getFoodCode() != null)
                .forEach(f -> {
                    FoodSearch foodSearch = new FoodSearch(f, CriteriaType.FOOD_CODE);
                    foodCodeFoods.putIfAbsent(foodSearch.getFoodCode(), foodSearch);
                });
    }

    static void put(Food food, CriteriaType criteriaType, boolean allowDistribute) {
        switch (criteriaType) {
            case NAME -> {
                nameFoods.putIfAbsent(food.getFoodSearchCriteria(), food);
                if (allowDistribute) {
                    distribute(food);
                }
            }
            case BARCODE -> gtinUpcFoods.putIfAbsent(food.getGtinUpc(), food);
            case FOOD_CODE -> foodCodeFoods.putIfAbsent(food.getFoodCode(), food);
            case FDC_ID -> fdcIdFoods.putIfAbsent(food.getFdcId(), food);
        }
    }

    static void clearAll() {
        nameFoods.clear();
        gtinUpcFoods.clear();
        foodCodeFoods.clear();
        fdcIdFoods.clear();
    }

    Food getFoodByName(String name) {
        return nameFoods.get(name);
    }

    Food getFoodByBarcode(String barcode) {
        return gtinUpcFoods.get(barcode);
    }

    Food getFoodByFoodCode(String foodCode) {
        return foodCodeFoods.get(foodCode);
    }

    Food getFoodByFdcId(int fdcId) {
        return fdcIdFoods.get(fdcId);
    }
}
