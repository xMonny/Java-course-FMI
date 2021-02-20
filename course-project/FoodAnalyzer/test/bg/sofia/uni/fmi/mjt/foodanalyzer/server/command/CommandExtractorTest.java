package bg.sofia.uni.fmi.mjt.foodanalyzer.server.command;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.storage.FoodStorageAccessor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CommandExtractorTest {

    private static final FoodStorageAccessor accessor = new FoodStorageAccessor();

    @Test
    public void testExtractByFoodNameCommandWithValidName() {
        CommandExtractor extractor = new CommandExtractor(accessor, "get-food name");
        Command command = extractor.extract();
        assertEquals(new ReceiveFoodByNameCommand(accessor, "name", false)
                , command);
    }

    @Test
    public void testExtractByFoodNameCommandWithNameFromDigits() {
        CommandExtractor extractor = new CommandExtractor(accessor, "get-food 111");
        Command command = extractor.extract();
        assertEquals(new ReceiveFoodByNameCommand(accessor, "111", false)
                , command);
    }

    @Test
    public void testExtractByFoodBarcodeCommandWithCodeWithoutExtraNutrients() {
        CommandExtractor extractor = new CommandExtractor(accessor, "get-food-by-barcode --code=0");
        Command command = extractor.extract();
        assertEquals(new ReceiveFoodByBarcodeCommand(accessor, "0", false)
                , command);
    }

    @Test
    public void testExtractByFoodBarcodeCommandWithCodeWithExtraNutrients() {
        CommandExtractor extractor = new CommandExtractor(accessor, "get-food-by-barcode -n --code=0");
        Command command = extractor.extract();
        assertEquals(new ReceiveFoodByBarcodeCommand(accessor, "0", true)
                , command);
    }

    @Test
    public void testExtractByFoodBarcodeCommandWithImageWithoutExtraNutrients() {
        CommandExtractor extractor = new CommandExtractor(accessor, "get-food-by-barcode --img=path");
        Command command = extractor.extract();
        assertEquals(new ReceiveFoodByImageCommand(accessor, "path", false)
                , command);
    }

    @Test
    public void testExtractByFoodBarcodeCommandWithImageWithExtraNutrients() {
        CommandExtractor extractor = new CommandExtractor(accessor, "get-food-by-barcode -n --img=path");
        Command command = extractor.extract();
        assertEquals(new ReceiveFoodByImageCommand(accessor, "path", true)
                , command);
    }

    @Test
    public void testExtractByFoodBarcodeCommandWithCodeAndImageWithoutExtraNutrients() {
        CommandExtractor extractor = new CommandExtractor(accessor, "get-food-by-barcode --code=0 --img=path");
        Command command = extractor.extract();
        assertEquals("Key should be code, not image path. Code is with priority"
                , new ReceiveFoodByBarcodeCommand(accessor, "0", false)
                , command);
    }

    @Test
    public void testExtractByFoodBarcodeCommandWithCodeAndImageWithExtraNutrients() {
        CommandExtractor extractor = new CommandExtractor(accessor, "get-food-by-barcode -n --code=0 --img=path");
        Command command = extractor.extract();
        assertEquals("Key should be code, not image path. Code is with priority"
                , new ReceiveFoodByBarcodeCommand(accessor, "0", true)
                , command);
    }

    @Test
    public void testExtractByFoodCodeCommandWithoutExtraNutrients() {
        CommandExtractor extractor = new CommandExtractor(accessor, "get-food-by-fc 0");
        Command command = extractor.extract();
        assertEquals(new ReceiveFoodByFoodCodeCommand(accessor, "0", false)
                , command);
    }

    @Test
    public void testExtractByFoodCodeCommandWithExtraNutrients() {
        CommandExtractor extractor = new CommandExtractor(accessor, "get-food-by-fc -n 0");
        Command command = extractor.extract();
        assertEquals(new ReceiveFoodByFoodCodeCommand(accessor, "0", true)
                , command);
    }

    @Test
    public void testExtractByFoodReportCommandWithoutExtraNutrients() {
        CommandExtractor extractor = new CommandExtractor(accessor, "get-food-report 0");
        Command command = extractor.extract();
        assertEquals(new ReceiveFoodByReportCommand(accessor, "0", false)
                , command);
    }

    @Test
    public void testExtractByFoodReportCommandWithExtraNutrients() {
        CommandExtractor extractor = new CommandExtractor(accessor, "get-food-report -n 0");
        Command command = extractor.extract();
        assertEquals(new ReceiveFoodByReportCommand(accessor, "0", true)
                , command);
    }

    @Test
    public void testExtractByInvalidCommand() {
        CommandExtractor extractor = new CommandExtractor(accessor, "bla bla");
        Command command = extractor.extract();
        assertEquals(new InvalidCommand(accessor, "none", false), command);
    }

    @Test
    public void testExtractByDisconnectCommand() {
        CommandExtractor extractor = new CommandExtractor(accessor, "disconnect");
        Command command = extractor.extract();
        assertEquals(new DisconnectCommand(accessor, "none", false), command);
    }
}
