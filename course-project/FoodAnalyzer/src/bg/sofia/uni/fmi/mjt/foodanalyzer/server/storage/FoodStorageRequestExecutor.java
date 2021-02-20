package bg.sofia.uni.fmi.mjt.foodanalyzer.server.storage;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.Food;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.type.CriteriaType;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.handler.RequestHandler;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.handler.exception.request.FoodException;

import java.net.http.HttpClient;

final class FoodStorageRequestExecutor {

    private final RequestHandler requestHandler;

    public FoodStorageRequestExecutor() {
        HttpClient httpClient = HttpClient.newBuilder().build();
        this.requestHandler = new RequestHandler(httpClient);
    }

    /**
     * Make request for food with @param name. If found food is not null, it will be saved
     * @param name - string which will be used as a key to search for Food
     * @return Food from made request or null if found information is not suitable
     * @throws FoodException if Food cannot be found or cannot be extracted
     */
    Food getInformationByName(String name) throws FoodException {
        Food food = requestHandler.handleSearchRequest(name);
        if (food != null) {
            FoodStorage.add(food, CriteriaType.NAME);
        }
        return food;
    }

    /**
     * Make request for food with @param name. If found food is not null, it will be saved
     * @param barcode - string which will be used as a key to search for Food
     * @return Food from made request or null if found information is not suitable
     * @throws FoodException if Food cannot be found or cannot be extracted
     */
    Food getInformationByBarcode(String barcode) throws FoodException {
        Food food = requestHandler.handleSearchRequest(barcode);
        if (food != null) {
            FoodStorage.add(food, CriteriaType.BARCODE);
        }
        return food;
    }

    /**
     * Make request for food with @param name. If found food is not null, it will be saved
     * @param foodCode - string which will be used as a key to search for Food
     * @return Food from made request or null if found information is not suitable
     * @throws FoodException if Food cannot be found or cannot be extracted
     */
    Food getInformationByFoodCode(String foodCode) throws FoodException {
        Food food = requestHandler.handleSearchRequest(foodCode);
        if (food != null) {
            FoodStorage.add(food, CriteriaType.FOOD_CODE);
        }
        return food;
    }

    /**
     * Make request for food with @param name. If found food is not null, it will be saved
     * @param fdcId - id which will be used as a key to search for Food
     * @return - Parameter which will be used as a key to find report for Food
     * @throws FoodException if Food cannot be found or cannot be extracted
     */
    Food getInformationByFdcId(int fdcId) throws FoodException {
        Food food = requestHandler.handleReportRequest(fdcId);
        if (food != null) {
            FoodStorage.add(food, CriteriaType.FDC_ID);
        }
        return food;
    }
}
