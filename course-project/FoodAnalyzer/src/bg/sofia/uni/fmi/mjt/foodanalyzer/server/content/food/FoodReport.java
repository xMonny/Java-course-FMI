package bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.nutrient.Nutrient;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.nutrient.NutrientContainer;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.format.Inclusion.include;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.format.Inclusion.includeExtraNutrients;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.text.Label.CLOSE_BRACKET;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.text.Label.CONTENT_LABEL;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.text.Label.FOOD_LABEL;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.text.Label.OPEN_BRACKET;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.text.Label.REPORT_RESULT_LABEL;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.text.Message.NUTRIENTS_CONTENT_NOT_FOUND;

public final class FoodReport extends Food implements Serializable {

    //collects each nutrient data in "nutrient"
    //NutrientContainer contains variable extraNutrient
    //where information (nutrientId, nutrientName, unitName, value)
    //is stored for each nutrient
    //We need it if client wants to see extended information
    //which includes nutrient's data (nutrientId, nutrientName, unitName, value)
    private final Set<NutrientContainer> foodNutrients;

    //represents information about total value of
    //fat, carbohydrates, protein, fiber, calories
    private final NutrientContainer labelNutrients;

    public FoodReport(int fdcId, String description, String dataType, String gtinUpc, String foodCode,
                      String ingredients, Set<NutrientContainer> foodNutrients, NutrientContainer labelNutrients) {
        super(fdcId, description, dataType, gtinUpc, foodCode, ingredients);
        this.foodNutrients = foodNutrients;
        this.labelNutrients = labelNutrients;
    }

    public Set<NutrientContainer> getFoodNutrients() {
        return this.foodNutrients;
    }

    public NutrientContainer getLabelNutrients() {
        return this.labelNutrients;
    }

    @Override
    public int getTotal() {
        return 1;
    }

    @Override
    public String getFoodSearchCriteria() {
        return String.valueOf(super.getFdcId());
    }

    @Override
    public Set<FoodSearch> getSearchFoods() {
        return null;
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
        FoodReport that = (FoodReport) o;
        return Objects.equals(foodNutrients, that.foodNutrients)
                && Objects.equals(labelNutrients, that.labelNutrients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), foodNutrients, labelNutrients);
    }

    @Override
    public String toString() {
        return toString(0, true);
    }

    @Override
    public String toString(boolean extraNutrientIncluded) {
        return toString(0, extraNutrientIncluded);
    }

    private void showLabelNutrients(int padding, StringBuilder builder) {
        if (labelNutrients == null) {
            include(padding, NUTRIENTS_CONTENT_NOT_FOUND, builder, true);
        } else {
            include(padding, CONTENT_LABEL, builder, true);
            include(padding, OPEN_BRACKET, builder, true);

            builder.append(labelNutrients.toString(padding + 2));

            include(padding, CLOSE_BRACKET, builder, true);
        }
    }

    private void showExtraNutrients(int padding, StringBuilder builder) {
        if (foodNutrients != null) {
            Set<Nutrient> extraNutrients = foodNutrients.stream()
                    .map(NutrientContainer::getExtraNutrient)
                    .collect(Collectors.toSet());

            includeExtraNutrients(padding, extraNutrients, builder);
        }
    }

    private String toString(int padding, boolean extraNutrientIncluded) {
        StringBuilder builder = new StringBuilder();

        include(padding, REPORT_RESULT_LABEL, builder, false);
        include(padding, getFdcId(), builder, true);

        include(padding, FOOD_LABEL, builder, true);
        include(padding, OPEN_BRACKET, builder, true);

        String foodBaseInformation = super.toString(padding + 2);
        builder.append(foodBaseInformation);

        showLabelNutrients(padding + 2, builder);
        if (extraNutrientIncluded) {
            showExtraNutrients(padding + 2, builder);
        }

        include(padding, CLOSE_BRACKET, builder, true);

        return builder.toString();
    }
}
