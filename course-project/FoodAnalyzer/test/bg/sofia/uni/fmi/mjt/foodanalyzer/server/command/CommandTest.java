package bg.sofia.uni.fmi.mjt.foodanalyzer.server.command;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.Criteria;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.Food;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.FoodReport;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.FoodSearch;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.handler.RequestHandler;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.handler.exception.request.FoodException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.handler.exception.request.FoodNotFoundException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.storage.FoodStorageAccessor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.Message.DISCONNECT_MESSAGE;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.Message.FOOD_PROBLEM_OCCURRED_MESSAGE;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.Message.FOUND_ZERO_FOODS_MESSAGE;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.Message.INVALID_BARCODE_FROM_IMAGE_MESSAGE;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.Message.INVALID_COMMAND_MESSAGE;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.Message.INVALID_FOOD_BARCODE_MESSAGE;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.Message.INVALID_FOOD_CODE_MESSAGE;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.Message.INVALID_FOOD_FDC_ID_MESSAGE;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.Message.INVALID_FOOD_NAME_MESSAGE;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.Message.INVALID_IMAGE_PATH_MESSAGE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CommandTest {

    private static final String FOOD_NOT_FOUND_MESSAGE = "Food not found";

    @Mock
    private FoodStorageAccessor foodStorageAccessorMock;

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullFoodStorageAccessor() {
        new ReceiveFoodByBarcodeCommand(null, "", false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullSearchKey() {
        new ReceiveFoodByBarcodeCommand(foodStorageAccessorMock, null, false);
    }

    @Test
    public void testGetResponseAfterInvalidCommand() {
        CommandExtractor commandExtractor = new CommandExtractor(foodStorageAccessorMock,"g");
        Command command = commandExtractor.extract();
        assertEquals(INVALID_COMMAND_MESSAGE, command.getResponse());
    }

    @Test
    public void testGetResponseAfterDisconnectCommand() {
        CommandExtractor commandExtractor = new CommandExtractor(foodStorageAccessorMock,"disconnect");
        Command command = commandExtractor.extract();
        assertEquals(DISCONNECT_MESSAGE, command.getResponse());
    }


    @Test
    public void testGetResponseByValidFoodName() throws FoodException {
        CommandExtractor commandExtractor = new CommandExtractor(foodStorageAccessorMock,"get-food raffaello");
        Command command = commandExtractor.extract();
        Food foodRaffaello = new FoodSearch(1, new Criteria("raffaello")
                , null, 0, null, null, null, null
                , null, null);
        when(foodStorageAccessorMock.extractFoodByName("raffaello")).thenReturn(foodRaffaello);
        assertEquals(foodRaffaello.toString(false), command.getResponse());
    }

    @Test
    public void testGetResponseByValidFoodNameWhenTotalIsZero() throws FoodException {
        CommandExtractor commandExtractor = new CommandExtractor(foodStorageAccessorMock,"get-food raffaello");
        Command command = commandExtractor.extract();
        Food foodRaffaello = new FoodSearch(0, new Criteria("raffaello")
                , null, 0, null, null, null, null
                , null, null);
        when(foodStorageAccessorMock.extractFoodByName("raffaello")).thenReturn(foodRaffaello);
        assertEquals(FOUND_ZERO_FOODS_MESSAGE, command.getResponse());
    }

    @Test
    public void testGetResponseNotMatchingFoodName() {
        CommandExtractor commandExtractor = new CommandExtractor(foodStorageAccessorMock,"get-food 1");
        Command command = commandExtractor.extract();

        assertEquals(INVALID_FOOD_NAME_MESSAGE, command.getResponse());
    }

    @Test
    public void testGetResponseByNameFoodNotFound() throws FoodException {
        CommandExtractor commandExtractor = new CommandExtractor(foodStorageAccessorMock,"get-food raffaello");
        Command command = commandExtractor.extract();
        when(foodStorageAccessorMock
                .extractFoodByName("raffaello"))
                .thenThrow(new FoodNotFoundException(FOOD_NOT_FOUND_MESSAGE));

        assertEquals(FOOD_PROBLEM_OCCURRED_MESSAGE + FOOD_NOT_FOUND_MESSAGE, command.getResponse());
    }

    @Test
    public void testGetResponseByValidBarcode() throws FoodException {
        CommandExtractor commandExtractor = new CommandExtractor(foodStorageAccessorMock,"get-food-by-barcode --code=012345678910");
        Command command = commandExtractor.extract();
        Food foodBarcode = new FoodSearch(1, new Criteria("012345678910")
                , null, 0, null, null, "012345678910", null
                , null, null);
        when(foodStorageAccessorMock.extractFoodByBarcode("012345678910")).thenReturn(foodBarcode);
        assertEquals(foodBarcode.toString(false), command.getResponse());
    }

    @Test
    public void testGetResponseByValidBarcodeWhenTotalIsZero() throws FoodException {
        CommandExtractor commandExtractor = new CommandExtractor(foodStorageAccessorMock,"get-food-by-barcode --code=012345678910");
        Command command = commandExtractor.extract();
        Food foodBarcode = new FoodSearch(0, new Criteria("012345678910")
                , null, 0, null, null, "012345678910", null
                , null, null);
        when(foodStorageAccessorMock.extractFoodByBarcode("012345678910")).thenReturn(foodBarcode);
        assertEquals(FOUND_ZERO_FOODS_MESSAGE, command.getResponse());
    }

    @Test
    public void testGetResponseByInValidBarcode() {
        CommandExtractor commandExtractor = new CommandExtractor(foodStorageAccessorMock,"get-food-by-barcode --code=0");
        Command command = commandExtractor.extract();

        assertEquals(INVALID_FOOD_BARCODE_MESSAGE, command.getResponse());
    }

    @Test
    public void testGetResponseByBarcodeFoodNotFound() throws FoodException {
        CommandExtractor commandExtractor = new CommandExtractor(foodStorageAccessorMock,"get-food-by-barcode --code=009800146130");
        Command command = commandExtractor.extract();
        when(foodStorageAccessorMock
                .extractFoodByBarcode("009800146130"))
                .thenThrow(new FoodNotFoundException(FOOD_NOT_FOUND_MESSAGE));

        assertEquals(FOOD_PROBLEM_OCCURRED_MESSAGE + FOOD_NOT_FOUND_MESSAGE, command.getResponse());
    }

    @Test
    public void testGetResponseByInvalidImagePath() {
        CommandExtractor commandExtractor = new CommandExtractor(foodStorageAccessorMock, "get-food-by-barcode --img=bla");
        Command command = commandExtractor.extract();

        assertEquals(INVALID_IMAGE_PATH_MESSAGE, command.getResponse());
    }

    @Test
    public void testGetResponseByValidImageBarcode() throws FoodException {
        CommandExtractor commandExtractor = new CommandExtractor(foodStorageAccessorMock, "get-food-by-barcode --img=resources/images/009800146130.gif");
        Command command = commandExtractor.extract();

        Food foodBarcode = new FoodSearch(1, new Criteria("009800146130")
                , null, 0, null, null, "009800146130", null
                , null, null);
        when(foodStorageAccessorMock.extractFoodByBarcode("009800146130")).thenReturn(foodBarcode);

        assertEquals(foodBarcode.toString(false), command.getResponse());
    }

    @Test
    public void testGetResponseByInvalidImageBarcode() {
        CommandExtractor commandExtractor = new CommandExtractor(foodStorageAccessorMock, "get-food-by-barcode --img=resources/images/ABC-abc-1234.gif");
        Command command = commandExtractor.extract();

        assertEquals("Problem for image barcode occurred: Barcode was not found in input image"
                , command.getResponse());
    }

    @Test
    public void testGetResponseByValidImageBarcodeWhenTotalIsZero() throws FoodException {
        CommandExtractor commandExtractor = new CommandExtractor(foodStorageAccessorMock, "get-food-by-barcode --img=resources/images/009800146130.gif");
        Command command = commandExtractor.extract();

        Food foodBarcode = new FoodSearch(0, new Criteria("009800146130")
                , null, 0, null, null, "009800146130", null
                , null, null);
        when(foodStorageAccessorMock.extractFoodByBarcode("009800146130")).thenReturn(foodBarcode);

        assertEquals(FOUND_ZERO_FOODS_MESSAGE, command.getResponse());
    }

    @Test
    public void testGetResponseByImageBarcodeFoodNotFound() throws FoodException {
        CommandExtractor commandExtractor = new CommandExtractor(foodStorageAccessorMock,"get-food-by-barcode --img=resources/images/009800146130.gif");
        Command command = commandExtractor.extract();
        when(foodStorageAccessorMock
                .extractFoodByBarcode("009800146130"))
                .thenThrow(new FoodNotFoundException(FOOD_NOT_FOUND_MESSAGE));

        assertEquals(FOOD_PROBLEM_OCCURRED_MESSAGE + FOOD_NOT_FOUND_MESSAGE, command.getResponse());
    }

    @Test
    public void testGetResponseByValidFoodCode() throws FoodException {
        CommandExtractor commandExtractor = new CommandExtractor(foodStorageAccessorMock,"get-food-by-fc 12345678");
        Command command = commandExtractor.extract();
        Food foodCode = new FoodSearch(1, new Criteria("12345678")
                , null, 0, null, null, null, "12345678"
                , null, null);
        when(foodStorageAccessorMock.extractFoodByFoodCode("12345678")).thenReturn(foodCode);
        assertEquals(foodCode.toString(false), command.getResponse());
    }

    @Test
    public void testGetResponseByValidFoodCodeWhenTotalIsZero() throws FoodException {
        CommandExtractor commandExtractor = new CommandExtractor(foodStorageAccessorMock,"get-food-by-fc 11111111");
        Command command = commandExtractor.extract();
        Food foodCode = new FoodSearch(0, new Criteria("11111111")
                , null, 0, null, null, null, "11111111"
                , null, null);
        when(foodStorageAccessorMock.extractFoodByFoodCode("11111111")).thenReturn(foodCode);
        assertEquals(FOUND_ZERO_FOODS_MESSAGE, command.getResponse());
    }

    @Test
    public void testGetResponseNotMatchingFoodCode() {
        CommandExtractor commandExtractor = new CommandExtractor(foodStorageAccessorMock,"get-food-by-fc 123456789");
        Command command = commandExtractor.extract();

        assertEquals(INVALID_FOOD_CODE_MESSAGE, command.getResponse());
    }

    @Test
    public void testGetResponseByFoodCodeFoodNotFound() throws FoodException {
        CommandExtractor commandExtractor = new CommandExtractor(foodStorageAccessorMock,"get-food-by-fc 12345678");
        Command command = commandExtractor.extract();
        when(foodStorageAccessorMock
                .extractFoodByFoodCode("12345678"))
                .thenThrow(new FoodNotFoundException(FOOD_NOT_FOUND_MESSAGE));

        assertEquals(FOOD_PROBLEM_OCCURRED_MESSAGE + FOOD_NOT_FOUND_MESSAGE, command.getResponse());
    }

    @Test
    public void testGetResponseByValidFdcId() throws FoodException {
        CommandExtractor commandExtractor = new CommandExtractor(foodStorageAccessorMock,"get-food-report 123456");
        Command command = commandExtractor.extract();
        Food foodReport = new FoodReport(123456, null, null
                , null, null, null, null, null);
        when(foodStorageAccessorMock.extractFoodByReport(123456)).thenReturn(foodReport);
        assertEquals(foodReport.toString(false), command.getResponse());
    }

    @Test
    public void testGetResponseNotMatchingFdcId() {
        CommandExtractor commandExtractor = new CommandExtractor(foodStorageAccessorMock,"get-food-report 1234567");
        Command command = commandExtractor.extract();

        assertEquals(INVALID_FOOD_FDC_ID_MESSAGE, command.getResponse());
    }

    @Test
    public void testGetResponseByFdcIdFoodNotFound() throws FoodException {
        CommandExtractor commandExtractor = new CommandExtractor(foodStorageAccessorMock,"get-food-report 123456");
        Command command = commandExtractor.extract();
        when(foodStorageAccessorMock
                .extractFoodByReport(123456))
                .thenThrow(new FoodNotFoundException(FOOD_NOT_FOUND_MESSAGE));

        assertEquals(FOOD_PROBLEM_OCCURRED_MESSAGE + FOOD_NOT_FOUND_MESSAGE, command.getResponse());
    }
}
