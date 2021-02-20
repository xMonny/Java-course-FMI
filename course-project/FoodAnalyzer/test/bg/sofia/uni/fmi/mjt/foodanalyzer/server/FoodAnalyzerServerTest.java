package bg.sofia.uni.fmi.mjt.foodanalyzer.server;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FoodAnalyzerServerTest {

    @Test(expected = IllegalArgumentException.class)
    public void testFoodAnalyzerServerWithNegativePort() {
        new FoodAnalyzerServer(-10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFoodAnalyzerServerWithLargerPort() {
        new FoodAnalyzerServer(100000);
    }

    @Test
    public void testStart() {
        FoodAnalyzerServer server = new FoodAnalyzerServer(3333);
        server.start();
        boolean hasStarted = server.isStarted();
        server.stop();
        assertTrue(hasStarted);
    }

    @Test
    public void testStop() throws InterruptedException {
        FoodAnalyzerServer server = new FoodAnalyzerServer(3333);
        server.start();
        Thread.sleep(4);
        server.stop();
        assertFalse(server.isStarted());
    }
}
