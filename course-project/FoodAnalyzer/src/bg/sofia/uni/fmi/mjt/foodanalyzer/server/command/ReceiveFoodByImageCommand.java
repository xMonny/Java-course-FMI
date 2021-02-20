package bg.sofia.uni.fmi.mjt.foodanalyzer.server.command;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.Food;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.handler.ImageHandler;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.handler.exception.barcode.BarcodeException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.handler.exception.request.FoodException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.storage.FoodStorageAccessor;

import java.io.File;

import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.KeyPattern.BARCODE_PATTERN;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.Message.FOOD_PROBLEM_OCCURRED_MESSAGE;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.Message.FOUND_ZERO_FOODS_MESSAGE;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.Message.IMAGE_BARCODE_PROBLEM_OCCURRED_MESSAGE;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.Message.INVALID_BARCODE_FROM_IMAGE_MESSAGE;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.Message.INVALID_IMAGE_PATH_MESSAGE;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.Message.NULL_BARCODE_MESSAGE;

public final class ReceiveFoodByImageCommand extends Command {

    public ReceiveFoodByImageCommand(FoodStorageAccessor accessor, String key,
                                     boolean extraNutrientIncluded) {
        super(accessor, key, extraNutrientIncluded);
    }

    private boolean imageExist() {
        File file = new File(key);
        return file.exists();
    }

    @Override
    public String getResponse() {
        if (!imageExist()) {
            return INVALID_IMAGE_PATH_MESSAGE;
        } else {
            String barcode;

            try {
                ImageHandler imageHandler = new ImageHandler();
                barcode = imageHandler.extractBarcode(key);
            } catch (BarcodeException e) {
                return IMAGE_BARCODE_PROBLEM_OCCURRED_MESSAGE + e.getMessage();
            }

            if (barcode == null) {
                return NULL_BARCODE_MESSAGE;
            }
            if (notMatching(barcode, BARCODE_PATTERN)) {
                return INVALID_BARCODE_FROM_IMAGE_MESSAGE;
            }

            try {
                Food foodByBarcode = foodStorageAccessor.extractFoodByBarcode(barcode);
                if (foodByBarcode.getTotal() == 0) {
                    return FOUND_ZERO_FOODS_MESSAGE;
                }
                return foodByBarcode.toString(extraNutrientIncluded);
            } catch (FoodException e) {
                return FOOD_PROBLEM_OCCURRED_MESSAGE + e.getMessage();
            }
        }
    }
}
