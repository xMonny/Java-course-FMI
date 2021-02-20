package bg.sofia.uni.fmi.mjt.foodanalyzer.server.storage;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.FoodReport;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.FoodSearch;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.type.CriteriaType;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.io.food.FoodReader;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.io.food.FoodWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.type.CriteriaType.BARCODE;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.type.CriteriaType.FDC_ID;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.type.CriteriaType.FOOD_CODE;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.type.CriteriaType.NAME;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.storage.FoodFileNames.BARCODE_FOOD_FILE;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.storage.FoodFileNames.FDC_ID_FOOD_FILE;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.storage.FoodFileNames.FOOD_CODE_FOOD_FILE;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.storage.FoodFileNames.NAME_FOOD_FILE;

final class FoodDataTransporter {

    private static final String READING_ERROR_MESSAGE = "Error occurred while reading source file";
    private static final String DESERIALIZE_ERROR_MESSAGE = "Error: Object cannot be deserialized while "
            + "reading source file";
    private static final String SAVE_FILE_NOT_FOUND_ERROR_MESSAGE = "Not found file to save information";
    private static final String SAVE_ERROR_MESSAGE = "Error occurred in saving information";

    private static void distribute(Set<Object> foods, CriteriaType criteriaType) {
        if (foods == null) {
            return;
        }
        for (Object object : foods) {
            if (object instanceof FoodSearch) {
                FoodSearch foodSearch = ((FoodSearch) object);
                FoodStorage.add(foodSearch, criteriaType);
            } else if (object instanceof FoodReport) {
                FoodReport foodReport = ((FoodReport) object);
                FoodStorage.add(foodReport, criteriaType);
            }
        }
    }

    static void loadData() {
        FoodReader foodNameReader = new FoodReader(NAME_FOOD_FILE);
        FoodReader foodBarcodeReader = new FoodReader(BARCODE_FOOD_FILE);
        FoodReader foodCodeReader = new FoodReader(FOOD_CODE_FOOD_FILE);
        FoodReader foodFdcIdReader = new FoodReader(FDC_ID_FOOD_FILE);

        try {
            distribute(foodNameReader.read(), NAME);
            distribute(foodBarcodeReader.read(), BARCODE);
            distribute(foodCodeReader.read(), FOOD_CODE);
            distribute(foodFdcIdReader.read(), FDC_ID);
        } catch (FileNotFoundException e) {
            return;
        } catch (IOException e) {
            System.err.println(READING_ERROR_MESSAGE);
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println(DESERIALIZE_ERROR_MESSAGE);
            e.printStackTrace();
        }

    }

    private static String selectFileName(CriteriaType criteriaType) {
        switch (criteriaType) {
            case NAME -> {
                return NAME_FOOD_FILE;
            }
            case BARCODE -> {
                return BARCODE_FOOD_FILE;
            }
            case FOOD_CODE -> {
                return FOOD_CODE_FOOD_FILE;
            }
            case FDC_ID -> {
                return FDC_ID_FOOD_FILE;
            }
            default -> {
                return null;
            }
        }
    }

    static void saveData(Object object, CriteriaType criteriaType) {
        String fileName = selectFileName(criteriaType);
        if (fileName == null) {
            return;
        }
        FoodWriter foodWriter = new FoodWriter(fileName);
        try {
            foodWriter.write(object);
        } catch (FileNotFoundException e) {
            System.err.println(SAVE_FILE_NOT_FOUND_ERROR_MESSAGE);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println(SAVE_ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
