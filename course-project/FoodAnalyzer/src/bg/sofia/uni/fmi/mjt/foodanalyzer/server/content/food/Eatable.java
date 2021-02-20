package bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food;

import java.util.Set;

public interface Eatable {

    /**
     * returns unique food id
     */
    int getFdcId();

    /**
     * returns food description
     */
    String getDescription();

    /**
     * returns food's data type - like "Branded", "Survey" and etc
     */
    String getDataType();

    /**
     * returns unique food's barcode if data type is branded or null otherwise
     */
    String getGtinUpc();

    /**
     * returns unique food's code if data type is NOT branded or null otherwise
     */
    String getFoodCode();

    /**
     * returns food's ingredients or null if food doesn't have ingredients
     */
    String getIngredients();

    /**
     * returns all found results
     */
    int getTotal();

    /**
     * returns search input
     */
    String getFoodSearchCriteria();

    /**
     * returns all found foods from searching request
     */
    Set<FoodSearch> getSearchFoods();

    /**
     * return food's information
     *
     * @param extraNutrientIncluded - if true, information for all included nutrients is shown, too;
     *                                if false, only food's base information is shown
     * @return food's base information or in detail
     */
    String toString(boolean extraNutrientIncluded);
}
