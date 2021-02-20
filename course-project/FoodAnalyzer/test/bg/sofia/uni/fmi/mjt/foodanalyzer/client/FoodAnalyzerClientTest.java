package bg.sofia.uni.fmi.mjt.foodanalyzer.client;

import org.junit.Test;

public class FoodAnalyzerClientTest {

    @Test(expected = IllegalArgumentException.class)
    public void testFoodAnalyzerClientWithNullHost() {
        new FoodAnalyzerClient(null, 4444);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFoodAnalyzerClientWithNegativePort() {
        new FoodAnalyzerClient("localhost", -10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFoodAnalyzerClientWithLargerPort() {
        new FoodAnalyzerClient("localhost", 100000);
    }
}
