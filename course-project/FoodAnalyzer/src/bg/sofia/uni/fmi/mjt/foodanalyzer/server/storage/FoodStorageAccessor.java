package bg.sofia.uni.fmi.mjt.foodanalyzer.server.storage;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.Food;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.handler.exception.request.FoodException;

public class FoodStorageAccessor {

    private final FoodStorageRequestExecutor requestExecutor = new FoodStorageRequestExecutor();

    private Food getFoodByName(String name) {
        return FoodStorage.getStorage().getFoodByName(name);
    }

    private Food getFoodByBarcode(String barcode) {
        return FoodStorage.getStorage().getFoodByBarcode(barcode);
    }

    private Food getFoodByFoodCode(String foodCode) {
        return FoodStorage.getStorage().getFoodByFoodCode(foodCode);
    }

    private Food getFoodByReport(int fdcId) {
        return FoodStorage.getStorage().getFoodByFdcId(fdcId);
    }

    /**
     *
     * @param name - string which will be used as a key to search for Food
     * @return - Food from storage if available or from "Food Data Central" REST-API
     * @throws FoodException if food cannot be found or cannot be extracted
     */
    public Food extractFoodByName(String name) throws FoodException {
        Food foodByName = getFoodByName(name);
        if (foodByName != null) {
            return foodByName;
        }
        return requestExecutor.getInformationByName(name);
    }

    /**
     *
     * @param barcode - string which will be used as a key to search for Food
     * @return - Food from storage if available or from "Food Data Central" REST-API
     * @throws FoodException if food cannot be found or cannot be extracted
     */
    public Food extractFoodByBarcode(String barcode) throws FoodException {
        Food foodByBarcode = getFoodByBarcode(barcode);
        if (foodByBarcode != null) {
            return foodByBarcode;
        }
        return requestExecutor.getInformationByBarcode(barcode);
    }

    /**
     *
     * @param foodCode - string which will be used as a key to search for Food
     * @return - Food from storage if available or from "Food Data Central" REST-API
     * @throws FoodException if food cannot be found or cannot be extracted
     */
    public Food extractFoodByFoodCode(String foodCode) throws FoodException {
        Food foodByFoodCode = getFoodByFoodCode(foodCode);
        if (foodByFoodCode != null) {
            return foodByFoodCode;
        }
        return requestExecutor.getInformationByFoodCode(foodCode);
    }

    /**
     *
     * @param fdcId - id which will be used as a key to find Food report
     * @return - Food from storage if available or from "Food Data Central" REST-API
     * @throws FoodException if food cannot be found or cannot be extracted
     */
    public Food extractFoodByReport(int fdcId) throws FoodException {
        Food foodByReport = getFoodByReport(fdcId);
        if (foodByReport != null) {
            return foodByReport;
        }
        return requestExecutor.getInformationByFdcId(fdcId);
    }
}
