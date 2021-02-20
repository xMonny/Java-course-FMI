package bg.sofia.uni.fmi.mjt.foodanalyzer.server.storage;

import java.io.File;

interface FoodFileNames {

    String FILE_SEPARATOR = File.separator;
    String BASE_PATH = "resources" + FILE_SEPARATOR + "storage" + FILE_SEPARATOR;

    String NAME_FOOD_FILE = BASE_PATH + "name_food.txt";
    String BARCODE_FOOD_FILE = BASE_PATH + "barcode_food.txt";
    String FOOD_CODE_FOOD_FILE = BASE_PATH + "foodCode_food.txt";
    String FDC_ID_FOOD_FILE = BASE_PATH + "fdcId_food.txt";
}
