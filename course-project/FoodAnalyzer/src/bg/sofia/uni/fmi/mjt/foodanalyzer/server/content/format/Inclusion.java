package bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.format;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.nutrient.Nutrient;

import java.util.Iterator;
import java.util.Set;

import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.text.Label.CLOSE_BRACKET;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.text.Label.LINE_SEPARATOR;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.text.Label.NUTRIENTS_LABEL;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.text.Label.OPEN_BRACKET;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.text.Message.EXTRA_NUTRIENTS_NOT_FOUND;

public final class Inclusion {

    private Inclusion() {
    }

    public static <T> void include(int padding, T information, StringBuilder builder, boolean appendLineSeparator) {
        builder.append("\s".repeat(Math.max(0, padding)));
        builder.append(information);
        if (appendLineSeparator) {
            builder.append(LINE_SEPARATOR);
        }
    }

    public static <T> void include(int padding, String labelType, T information, StringBuilder builder,
                                   boolean appendLineSeparator) {
        builder.append("\s".repeat(Math.max(0, padding)));
        builder.append(labelType);
        builder.append(information);
        if (appendLineSeparator) {
            builder.append(LINE_SEPARATOR);
        }
    }

    private static void includeNewLine(StringBuilder builder) {
        builder.append(LINE_SEPARATOR);
    }

    public static void includeExtraNutrients(int padding, Set<Nutrient> foodNutrients, StringBuilder builder) {
        if (foodNutrients == null) {
            include(padding, EXTRA_NUTRIENTS_NOT_FOUND, builder, true);
        } else {
            include(padding, NUTRIENTS_LABEL, builder, true);
            include(padding, OPEN_BRACKET, builder, true);

            Iterator<Nutrient> iterator = foodNutrients.iterator();
            while (iterator.hasNext()) {
                Nutrient currentNutrient = iterator.next();
                String currentNutrientInformation = currentNutrient.toString(padding + 2);
                builder.append(currentNutrientInformation);
                if (iterator.hasNext()) {
                    includeNewLine(builder);
                }
            }

            include(padding, CLOSE_BRACKET, builder, true);
        }
    }
}
