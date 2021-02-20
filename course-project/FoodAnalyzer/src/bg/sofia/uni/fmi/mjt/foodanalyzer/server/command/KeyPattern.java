package bg.sofia.uni.fmi.mjt.foodanalyzer.server.command;

interface KeyPattern {

    String FOOD_NAME_PATTERN = "^([a-zA-Z,]+ ?)+$";
    String BARCODE_PATTERN = "^[0-9]{12}$";
    String FOOD_CODE_PATTERN = "^[1-9]{1}[0-9]{7}$";
    String FDC_ID_PATTERN = "^[1-9]{1}[0-9]{5}$";
}

