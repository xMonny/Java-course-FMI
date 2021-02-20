package bg.sofia.uni.fmi.mjt.foodanalyzer.server.io.food;

import org.junit.Test;

public class FoodWriterTest {

    @Test(expected = IllegalArgumentException.class)
    public void testFoodWriterWhenFileNameIsNull() {
        new FoodWriter(null);
    }
}
