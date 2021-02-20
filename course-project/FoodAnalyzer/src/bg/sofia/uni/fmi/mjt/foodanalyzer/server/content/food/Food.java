package bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food;

import java.io.Serializable;
import java.util.Objects;

import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.format.Inclusion.include;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.text.Label.FOOD_BARCODE_LABEL;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.text.Label.FOOD_CODE_LABEL;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.text.Label.FOOD_DATA_TYPE_LABEL;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.text.Label.FOOD_DESCRIPTION_LABEL;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.text.Label.FOOD_FDC_ID_LABEL;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.text.Label.FOOD_INGREDIENTS_LABEL;

//Contains part of common data which is represented
//in all kind of searches and report
public abstract class Food implements Eatable, Serializable {

    private final int fdcId;
    private final String description;
    private final String dataType;
    private final String gtinUpc;
    private final String foodCode;
    private final String ingredients;

    public Food(int fdcId, String description, String dataType, String gtinUpc, String foodCode, String ingredients) {
        this.fdcId = fdcId;
        this.description = description;
        this.dataType = dataType;
        this.gtinUpc = gtinUpc;
        this.foodCode = foodCode;
        this.ingredients = ingredients;
    }

    @Override
    public int getFdcId() {
        return this.fdcId;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getDataType() {
        return this.dataType;
    }

    @Override
    public String getGtinUpc() {
        return this.gtinUpc;
    }

    @Override
    public String getFoodCode() {
        return this.foodCode;
    }

    @Override
    public String getIngredients() {
        return this.ingredients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Food food = (Food) o;
        return fdcId == food.fdcId
                && Objects.equals(description, food.description)
                && Objects.equals(dataType, food.dataType)
                && Objects.equals(gtinUpc, food.gtinUpc)
                && Objects.equals(ingredients, food.ingredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fdcId, description, dataType, gtinUpc, ingredients);
    }

    protected String toString(int padding) {
        StringBuilder builder = new StringBuilder();

        include(padding, FOOD_FDC_ID_LABEL, fdcId, builder, true);
        include(padding, FOOD_DESCRIPTION_LABEL, description, builder, true);
        include(padding, FOOD_DATA_TYPE_LABEL, dataType, builder, true);

        if (gtinUpc != null) {
            include(padding, FOOD_BARCODE_LABEL, gtinUpc, builder, true);
        }
        if (foodCode != null) {
            include(padding, FOOD_CODE_LABEL, foodCode, builder, true);
        }
        if (ingredients != null) {
            include(padding, FOOD_INGREDIENTS_LABEL, ingredients, builder, true);
        }

        return builder.toString();
    }
}
