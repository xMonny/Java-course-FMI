package bg.sofia.uni.fmi.mjt.foodanalyzer.server.command;

interface CommandPattern {

    String FOOD_BY_NAME_NO_NUTRIENTS_COMMAND = "^get-food [^-]+$";

    String FOOD_BY_BARCODE_NO_NUTRIENTS_COMMAND = "^get-food-by-barcode --code=[0-9]+$";
    String FOOD_BY_BARCODE_WITH_NUTRIENTS_COMMAND = "^get-food-by-barcode -n --code=[0-9]+$";

    String FOOD_BY_IMAGE_NO_NUTRIENTS_COMMAND = "^get-food-by-barcode --img=.+$";
    String FOOD_BY_IMAGE_WITH_NUTRIENTS_COMMAND = "^get-food-by-barcode -n --img=.+$";

    String FOOD_BY_IMAGE_BARCODE_NO_NUTRIENTS_COMMAND = "^get-food-by-barcode --code=[0-9]+ --img=.+$";
    String FOOD_BY_IMAGE_BARCODE_WITH_NUTRIENTS_COMMAND = "^get-food-by-barcode -n --code=[0-9]+ --img=.+$";

    String FOOD_BY_FOOD_CODE_NO_NUTRIENTS_COMMAND = "^get-food-by-fc [^-]+$";
    String FOOD_BY_FOOD_CODE_WITH_NUTRIENTS_COMMAND = "^get-food-by-fc -n [^-]+$";

    String FOOD_BY_FDC_ID_NO_NUTRIENTS_COMMAND = "^get-food-report [^-]+$";
    String FOOD_BY_FDC_ID_WITH_NUTRIENTS_COMMAND = "^get-food-report -n [^-]+$";

    String DISCONNECT_COMMAND = "^disconnect$";
}
