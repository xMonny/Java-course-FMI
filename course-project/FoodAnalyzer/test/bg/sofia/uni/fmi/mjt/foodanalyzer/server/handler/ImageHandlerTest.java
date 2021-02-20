package bg.sofia.uni.fmi.mjt.foodanalyzer.server.handler;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.handler.exception.barcode.BarcodeException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.handler.exception.barcode.BarcodeNotFoundException;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class ImageHandlerTest {

    private static final String FILE_SEPARATOR = File.separator;
    private static final String BASE_PATH = "resources" + FILE_SEPARATOR + "images" + FILE_SEPARATOR;
    private static final String INVALID_BARCODE_IMAGE_PATH = BASE_PATH + "ABC-abc-1234.gif";
    private static final String VALID_BARCODE_IMAGE_PATH = BASE_PATH + "009800146130.gif";

    @Test(expected = BarcodeNotFoundException.class)
    public void testExtractBarcodeWithInvalidBarcodeInImage() throws BarcodeException {
        ImageHandler imageHandler = new ImageHandler();
        imageHandler.extractBarcode(INVALID_BARCODE_IMAGE_PATH);
    }

    @Test
    public void testExtractBarcodeWithValidBarcodeInImage() throws BarcodeException {
        ImageHandler imageHandler = new ImageHandler();
        String expected = "009800146130";
        String actual = imageHandler.extractBarcode(VALID_BARCODE_IMAGE_PATH);
        assertEquals("Barcodes should be equal", expected, actual);
    }
}
