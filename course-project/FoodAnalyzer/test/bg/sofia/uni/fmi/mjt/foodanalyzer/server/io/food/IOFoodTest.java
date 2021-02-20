package bg.sofia.uni.fmi.mjt.foodanalyzer.server.io.food;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.Criteria;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.Food;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.FoodReport;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.FoodSearch;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IOFoodTest {

    private static final String FILE_SEPARATOR = File.separator;
    private static final String BASE_PATH = "resources" + FILE_SEPARATOR + "test" + FILE_SEPARATOR;
    private static final String TEST_FILE_NAME = "testFile.txt";

    @Test
    public void testWritingReadingInEmptyFile() throws IOException, ClassNotFoundException {
        File tempFoodNameFile = File.createTempFile(BASE_PATH, TEST_FILE_NAME);
        tempFoodNameFile.deleteOnExit();

        FoodWriter foodWriter = new FoodWriter(TEST_FILE_NAME);
        Food foodRaffaello = new FoodSearch(1, new Criteria("raffaello")
                , null, 0, null, null, null, null
                , null, null);
        foodWriter.write(foodRaffaello);

        FoodReader foodReader = new FoodReader(TEST_FILE_NAME);
        Set<Object> readFoods = foodReader.read();
        Files.delete(Path.of(TEST_FILE_NAME));
        assertEquals(1, readFoods.size());
        assertTrue(readFoods.contains(foodRaffaello));
    }

    @Test
    public void testWritingReadingInNonEmptyFile() throws IOException, ClassNotFoundException {
        File tempFoodNameFile = File.createTempFile(BASE_PATH, TEST_FILE_NAME);
        tempFoodNameFile.deleteOnExit();

        FoodWriter foodWriter = new FoodWriter(TEST_FILE_NAME);
        Food foodRaffaello = new FoodSearch(1, new Criteria("raffaello")
                , null, 0, null, null, null, null
                , null, null);

        Food foodReport = new FoodReport(1, null, null, null
                , null, null, null, null);
        foodWriter.write(foodRaffaello);
        foodWriter.write(foodReport);

        FoodReader foodReader = new FoodReader(TEST_FILE_NAME);
        Set<Object> readFoods = foodReader.read();
        Files.delete(Path.of(TEST_FILE_NAME));
        assertEquals(2, readFoods.size());
        assertTrue(readFoods.contains(foodRaffaello));
        assertTrue(readFoods.contains(foodReport));
    }
}
