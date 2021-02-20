package bg.sofia.uni.fmi.mjt.foodanalyzer.server.storage;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.Food;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.FoodSearch;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.type.CriteriaType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final class FoodStorage {

    private static final Map<String, Food> nameFoods = new ConcurrentHashMap<>();
    private static final Map<String, Food> gtinUpcFoods = new ConcurrentHashMap<>();
    private static final Map<String, Food> foodCodeFoods = new ConcurrentHashMap<>();
    private static final Map<Integer, Food> fdcIdFoods = new ConcurrentHashMap<>();

    private static final FoodStorage FOOD_STORAGE = new FoodStorage();

    private FoodStorage() {
        FoodDataTransporter.loadData();
    }

    public static boolean isLoaded() {
        return FOOD_STORAGE != null;
    }

    static FoodStorage getStorage() {
        return FOOD_STORAGE;
    }

    /**
     *
     * @param food - Food which contains content of other foods. These with 'barcode' and 'food code'
     *             will be added in storage by its categories
     */
    private static void distribute(Food food) {
        food.getSearchFoods().stream()
                .filter(f -> f.getGtinUpc() != null)
                .forEach(f -> {
                    FoodSearch foodSearch = new FoodSearch(f, CriteriaType.BARCODE);
                    gtinUpcFoods.putIfAbsent(foodSearch.getGtinUpc(), foodSearch);
                    FoodDataTransporter.saveData(foodSearch, CriteriaType.BARCODE);
                });
        food.getSearchFoods().stream()
                .filter(f -> f.getFoodCode() != null)
                .forEach(f -> {
                    FoodSearch foodSearch = new FoodSearch(f, CriteriaType.FOOD_CODE);
                    foodCodeFoods.putIfAbsent(foodSearch.getFoodCode(), foodSearch);
                    FoodDataTransporter.saveData(foodSearch, CriteriaType.FOOD_CODE);
                });
    }

    /**
     *
     * @param food - Food which will be added in storage if is absent
     * @param criteriaType - Type which determines where {@param food} should be saved
     * @param allowDistribute - If true, food will distribute all of its content of other foods
     *                        in categories 'barcode' and 'food code'
     */
    private static void put(Food food, CriteriaType criteriaType, boolean allowDistribute) {
        switch (criteriaType) {
            case NAME -> {
                nameFoods.putIfAbsent(food.getFoodSearchCriteria(), food);
                if (allowDistribute) {
                    distribute(food);
                }
            }
            case BARCODE -> gtinUpcFoods.putIfAbsent(food.getFoodSearchCriteria(), food);
            case FOOD_CODE -> foodCodeFoods.putIfAbsent(food.getFoodSearchCriteria(), food);
            case FDC_ID -> fdcIdFoods.putIfAbsent(food.getFdcId(), food);
        }
    }

    /**
     * Add food to storage. The food is saved in special file after successful made request.
     * The food is only added in storage but not in file if storage is adding foods from files.
     * Last one happens when saved foods in special files should be read and added to storage.
     * @param food - Food which will be added to storage
     * @param criteriaType - Type which determines where {@param food} should be added
     */
    static void add(Food food, CriteriaType criteriaType) {
        if (!isLoaded()) {
            put(food, criteriaType, false);
        } else {
            put(food, criteriaType, true);
            FoodDataTransporter.saveData(food, criteriaType);
        }
    }

    /**
     *
     * @param name - string which will be used as a key for searching Food in storage
     * @return Food by key {@param name}
     */
    Food getFoodByName(String name) {
        return nameFoods.get(name);
    }

    /**
     *
     * @param barcode - string which will be used as a key for searching Food in storage
     * @return Food by key {@param barcode}
     */
    Food getFoodByBarcode(String barcode) {
        return gtinUpcFoods.get(barcode);
    }

    /**
     *
     * @param foodCode - string which will be used as a key for searching Food in storage
     * @return Food by key {@param foodCode}
     */
    Food getFoodByFoodCode(String foodCode) {
        return foodCodeFoods.get(foodCode);
    }

    /**
     *
     * @param fdcId - id which will be used as a key for searching Food in storage
     * @return Food by key {@param fdcId}
     */
    Food getFoodByFdcId(int fdcId) {
        return fdcIdFoods.get(fdcId);
    }
}
