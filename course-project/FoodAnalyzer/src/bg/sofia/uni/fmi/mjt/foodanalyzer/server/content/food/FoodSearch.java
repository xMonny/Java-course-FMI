package bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.type.CriteriaType;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.nutrient.Nutrient;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.format.Inclusion.include;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.format.Inclusion.includeExtraNutrients;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.text.Label.CLOSE_BRACKET;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.text.Label.FOOD_LABEL;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.text.Label.OPEN_BRACKET;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.text.Label.SEARCH_RESULT_LABEL;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.text.Label.TOTAL_LABEL;

public final class FoodSearch extends Food implements Serializable {

    @SerializedName("totalHits")
    private final int total;
    private final Criteria foodSearchCriteria;

    //In searching by name and description
    //all needed information is collected in "foods"
    private final Set<FoodSearch> foods;

    //searching by name and description contains
    //nutrients' data directly (nutrientId, nutrientName, unitName, value).
    //It is not like searching food by fdcId - then nutrients
    //are combined in "nutrient".
    //We need it if client wants to see extended information
    //which includes nutrient's data (nutrientId, nutrientName, unitName, value)
    private final Set<Nutrient> foodNutrients;

    public FoodSearch(int total, Criteria foodSearchCriteria, Set<FoodSearch> foods,
                      int fdcId, String description, String dataType, String gtinUpc, String foodCode,
                      String ingredients, Set<Nutrient> foodNutrients) {
        super(fdcId, description, dataType, gtinUpc, foodCode, ingredients);
        this.total = total;
        this.foodSearchCriteria = foodSearchCriteria;
        this.foods = foods;
        this.foodNutrients = foodNutrients;
    }

    public FoodSearch(FoodSearch foodSearch, CriteriaType criteriaType) {
        super(foodSearch.getFdcId(),
                foodSearch.getDescription(),
                foodSearch.getDataType(),
                foodSearch.getGtinUpc(),
                foodSearch.getFoodCode(),
                foodSearch.getIngredients());
        this.total = 1;
        this.foodSearchCriteria = defineFoodSearchCriteria(foodSearch, criteriaType);
        this.foods = Set.of(foodSearch);
        this.foodNutrients = foodSearch.getFoodNutrients();
    }

    private Criteria defineFoodSearchCriteria(FoodSearch foodSearch, CriteriaType criteriaType) {
        switch (criteriaType) {
            case BARCODE -> {
                return new Criteria(foodSearch.getGtinUpc());
            }
            case FOOD_CODE -> {
                return new Criteria(foodSearch.getFoodCode());
            }
            default -> throw new IllegalArgumentException("Food search criteria is not from allowed type");

        }
    }

    @Override
    public int getTotal() {
        return this.total;
    }

    @Override
    public String getFoodSearchCriteria() {
        return this.foodSearchCriteria.getName();
    }

    @Override
    public Set<FoodSearch> getSearchFoods() {
        return this.foods;
    }

    public Set<Nutrient> getFoodNutrients() {
        return this.foodNutrients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        FoodSearch that = (FoodSearch) o;
        return total == that.total
                && foodSearchCriteria.equals(that.foodSearchCriteria)
                && Objects.equals(foods, that.foods)
                && Objects.equals(foodNutrients, that.foodNutrients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), total, foodSearchCriteria,
                foods, foodNutrients);
    }

    @Override
    public String toString() {
        return toString(0, true);
    }

    @Override
    public String toString(boolean extraNutrientIncluded) {
        return toString(0, extraNutrientIncluded);
    }

    private String toString(int padding, boolean extraNutrientIncluded) {
        StringBuilder builder = new StringBuilder();

        include(padding, SEARCH_RESULT_LABEL, builder, false);

        if (foodSearchCriteria != null) {
            include(padding, foodSearchCriteria.getName(), builder, true);
        }

        include(padding, TOTAL_LABEL, total, builder, true);

        if (foods != null) {
            for (FoodSearch foodSearch : foods) {
                include(padding, FOOD_LABEL, builder, true);
                include(padding, OPEN_BRACKET, builder, true);

                String foodBaseInformation = foodSearch.toString(padding + 2);
                builder.append(foodBaseInformation);

                if (extraNutrientIncluded) {
                    includeExtraNutrients(padding + 2, foodSearch.getFoodNutrients(), builder);
                }

                include(padding, CLOSE_BRACKET, builder, true);
            }
        }

        return builder.toString();
    }
}
