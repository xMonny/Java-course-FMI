package bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.nutrient;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.format.Inclusion.include;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.text.Label.NUTRIENT_ID_LABEL;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.text.Label.NUTRIENT_NAME_LABEL;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.text.Label.NUTRIENT_UNIT_NAME_LABEL;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.text.Label.NUTRIENT_VALUE_LABEL;

//Contains nutrient's data which will be shown.
//If client wants to see extended information
//for nutrients, then nutrientId, nutrientName, unitName and value
//will be set and shown when food search or food report is called.
//If client doesn't want to see extended information
//for nutrients, then only total amount of
//fat, carbohydrates, protein, fiber and calories
//will be shown when food report is called.
public final class Nutrient implements Serializable {

    @SerializedName(value = "id", alternate = "nutrientId")
    private final int id;

    @SerializedName(value = "name", alternate = "nutrientName")
    private final String name;

    private final String unitName;

    private final double value;

    public Nutrient(int id, String name, String unitName, double value) {
        this.id = id;
        this.name = name;
        this.unitName = unitName;
        this.value = value;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getUnitName() {
        return this.unitName;
    }

    public double getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Nutrient nutrient = (Nutrient) o;
        return id == nutrient.id
                && Double.compare(nutrient.value, value) == 0
                && Objects.equals(name, nutrient.name)
                && Objects.equals(unitName, nutrient.unitName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, unitName, value);
    }

    @Override
    public String toString() {
        return toString(0);
    }

    public String toString(int padding) {
        StringBuilder builder = new StringBuilder();

        include(padding, NUTRIENT_ID_LABEL, id, builder, true);
        include(padding, NUTRIENT_NAME_LABEL, name, builder, true);
        include(padding, NUTRIENT_VALUE_LABEL, value, builder, true);
        include(padding, NUTRIENT_UNIT_NAME_LABEL, unitName, builder, true);

        return builder.toString();
    }
}
