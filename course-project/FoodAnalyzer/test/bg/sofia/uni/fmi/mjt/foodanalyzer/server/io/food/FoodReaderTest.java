package bg.sofia.uni.fmi.mjt.foodanalyzer.server.io.food;

import org.junit.Test;

public class FoodReaderTest {

    @Test(expected = IllegalArgumentException.class)
    public void testFoodReaderWhenFileNameIsNull() {
        new FoodReader(null);
    }
}
