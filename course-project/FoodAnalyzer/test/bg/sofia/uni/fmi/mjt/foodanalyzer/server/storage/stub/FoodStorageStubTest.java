package bg.sofia.uni.fmi.mjt.foodanalyzer.server.storage.stub;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.Criteria;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.Food;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.FoodReport;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.FoodSearch;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.type.CriteriaType;
import org.junit.After;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;

public class FoodStorageStubTest {
    @Test
    public void testPutReportByFdcId() {
        Food f = new FoodReport(0, null, null, null, null, null, null, null);
        FoodStorageStub.put(f, CriteriaType.FDC_ID, false);
        assertEquals(f, FoodStorageStub.getStorage().getFoodByFdcId(0));
    }

    @Test
    public void testPutSearchByNameWithoutDistribute() {
        Food f = new FoodSearch(0, new Criteria("input"), null, 0, null, null, null, null, null, null);
        FoodStorageStub.put(f, CriteriaType.NAME, false);
        assertEquals(f, FoodStorageStub.getStorage().getFoodByName("input"));
    }

    @Test
    public void testPutSearchByNameWithDistribute() {
        FoodSearch barcode_food = new FoodSearch(1, new Criteria("10"), null, 0, null, null, "10", null, null, null);
        FoodSearch fc_food = new FoodSearch(1, new Criteria("20"), null, 0, null, null, null, "20", null, null);
        Food f = new FoodSearch(0, new Criteria("input"), Set.of(barcode_food, fc_food), 0, null, null, null, null, null, null);
        FoodStorageStub.put(f, CriteriaType.NAME, true);

        FoodSearch from_barcode_food = new FoodSearch(barcode_food, CriteriaType.BARCODE);
        FoodSearch from_fc_food = new FoodSearch(fc_food, CriteriaType.FOOD_CODE);

        assertEquals(f, FoodStorageStub.getStorage().getFoodByName("input"));
        assertEquals(from_barcode_food, FoodStorageStub.getStorage().getFoodByBarcode("10"));
        assertEquals(from_fc_food, FoodStorageStub.getStorage().getFoodByFoodCode("20"));
    }

    @Test
    public void testPutSearchByBarcode() {
        Food f = new FoodSearch(0, new Criteria("10"), null, 0, null, null, "10", null, null, null);
        FoodStorageStub.put(f, CriteriaType.BARCODE, false);
        assertEquals(f, FoodStorageStub.getStorage().getFoodByBarcode("10"));
    }

    @Test
    public void testPutSearchByFoodCode() {
        Food f = new FoodSearch(0, new Criteria("10"), null, 0, null, null, null, "10", null, null);
        FoodStorageStub.put(f, CriteriaType.FOOD_CODE, false);
        assertEquals(f, FoodStorageStub.getStorage().getFoodByFoodCode("10"));
    }

    @After
    public void afterExecuting() {
        FoodStorageStub.clearAll();
    }
}
