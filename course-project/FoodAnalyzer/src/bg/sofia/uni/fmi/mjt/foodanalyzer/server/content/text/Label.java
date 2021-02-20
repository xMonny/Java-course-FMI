package bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.text;

public interface Label {

    String LINE_SEPARATOR = System.lineSeparator();

    String OPEN_BRACKET = "{";
    String CLOSE_BRACKET = "}";

    String REPORT_RESULT_LABEL = "REPORT RESULT: ";
    String SEARCH_RESULT_LABEL = "SEARCH RESULT: ";
    String TOTAL_LABEL = "Total: ";

    String FOOD_LABEL = "FOOD: ";
    String FOOD_FDC_ID_LABEL = "fdcId: ";
    String FOOD_DESCRIPTION_LABEL = "description: ";
    String FOOD_DATA_TYPE_LABEL = "dataType: ";
    String FOOD_BARCODE_LABEL = "gtinUpc: ";
    String FOOD_CODE_LABEL = "foodCode: ";
    String FOOD_INGREDIENTS_LABEL = "ingredients: ";

    String NUTRIENTS_LABEL = "NUTRIENTS: ";
    String NUTRIENT_ID_LABEL = "nutrientId: ";
    String NUTRIENT_NAME_LABEL = "nutrientName: ";
    String NUTRIENT_VALUE_LABEL = "value: ";
    String NUTRIENT_UNIT_NAME_LABEL = "unitName: ";

    String CONTENT_LABEL = "CONTENT: ";
    String NUTRIENT_FAT_LABEL = "fat: ";
    String NUTRIENT_PROTEIN_LABEL = "protein: ";
    String NUTRIENT_CARBOHYDRATES_LABEL = "carbohydrates: ";
    String NUTRIENT_FIBER_LABEL = "fiber: ";
    String NUTRIENT_CALORIES_LABEL = "calories: ";
}
