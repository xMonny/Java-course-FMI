package bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.nutrient;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.format.Inclusion.include;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.text.Label.NUTRIENT_CALORIES_LABEL;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.text.Label.NUTRIENT_CARBOHYDRATES_LABEL;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.text.Label.NUTRIENT_FAT_LABEL;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.text.Label.NUTRIENT_FIBER_LABEL;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.text.Label.NUTRIENT_PROTEIN_LABEL;

public final class NutrientContainer implements Serializable {

    private final Nutrient fat;
    private final Nutrient carbohydrates;
    private final Nutrient fiber;
    private final Nutrient protein;
    private final Nutrient calories;

    //this variable will hold detailed information
    //about nutrient's data like nutrientId, nutrientName, unitName and value
    //not only value such as other Nutrient variables
    @SerializedName("nutrient")
    private final Nutrient extraNutrient;

    public NutrientContainer(Nutrient fat, Nutrient carbohydrates, Nutrient fiber,
                             Nutrient protein, Nutrient calories, Nutrient extraNutrient) {
        this.fat = fat;
        this.carbohydrates = carbohydrates;
        this.fiber = fiber;
        this.protein = protein;
        this.calories = calories;
        this.extraNutrient = extraNutrient;
    }

    public Nutrient getFat() {
        return this.fat;
    }

    public Nutrient getCarbohydrates() {
        return this.carbohydrates;
    }

    public Nutrient getFiber() {
        return this.fiber;
    }

    public Nutrient getProtein() {
        return this.protein;
    }

    public Nutrient getCalories() {
        return this.calories;
    }

    public Nutrient getExtraNutrient() {
        return this.extraNutrient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NutrientContainer that = (NutrientContainer) o;
        return Objects.equals(fat, that.fat)
                && Objects.equals(carbohydrates, that.carbohydrates)
                && Objects.equals(protein, that.protein)
                && Objects.equals(fiber, that.fiber)
                && Objects.equals(calories, that.calories)
                && Objects.equals(extraNutrient, that.extraNutrient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fat, carbohydrates, protein, fiber, calories, extraNutrient);
    }

    @Override
    public String toString() {
        return toString(0);
    }

    public String toString(int padding) {
        StringBuilder builder = new StringBuilder();

        if (fat != null) {
            include(padding, NUTRIENT_FAT_LABEL, fat.getValue(), builder, true);
        }
        if (carbohydrates != null) {
            include(padding, NUTRIENT_CARBOHYDRATES_LABEL, carbohydrates.getValue(), builder,
                    true);
        }
        if (protein != null) {
            include(padding, NUTRIENT_PROTEIN_LABEL, protein.getValue(), builder, true);
        }
        if (fiber != null) {
            include(padding, NUTRIENT_FIBER_LABEL, fiber.getValue(), builder, true);
        }
        if (calories != null) {
            include(padding, NUTRIENT_CALORIES_LABEL, calories.getValue(), builder, true);
        }

        return builder.toString();
    }
}
