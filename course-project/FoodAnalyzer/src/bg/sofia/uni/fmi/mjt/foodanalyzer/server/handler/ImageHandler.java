package bg.sofia.uni.fmi.mjt.foodanalyzer.server.handler;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.handler.exception.barcode.BarcodeException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.handler.exception.barcode.BarcodeFormatException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.handler.exception.barcode.BarcodeNotFoundException;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.oned.UPCAReader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class ImageHandler {

    private static final String IMAGE_WRONG_READING_MESSAGE = "Error in reading input image";
    private static final String BARCODE_NOT_FOUND_MESSAGE = "Barcode was not found in input image";
    private static final String BARCODE_WRONG_FORMAT_MESSAGE = "Barcode was successfully detected, "
            + "but the content is not conformed with barcode's format rules";

    private BinaryBitmap generateBitmap(String imagePath) {
        BufferedImage bufferImage;
        BinaryBitmap binaryBitmap;

        try {
            bufferImage = ImageIO.read(new File(imagePath));
            int[] pixels = bufferImage.getRGB(0, 0,
                    bufferImage.getWidth(), bufferImage.getHeight(),
                    null, 0, bufferImage.getWidth());

            RGBLuminanceSource source = new RGBLuminanceSource(bufferImage.getWidth(),
                    bufferImage.getHeight(), pixels);
            binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));

        } catch (IOException e) {
            System.err.println(IMAGE_WRONG_READING_MESSAGE);
            e.printStackTrace();
            return null;
        }

        return binaryBitmap;
    }

    public String extractBarcode(String imagePath) throws BarcodeException {
        BinaryBitmap binaryBitmap = generateBitmap(imagePath);

        if (binaryBitmap == null) {
            return null;
        }

        UPCAReader reader = new UPCAReader();

        try {
            Result result = reader.decode(binaryBitmap);
            return result.getText();

        } catch (NotFoundException e) {
            throw new BarcodeNotFoundException(BARCODE_NOT_FOUND_MESSAGE, e);
        } catch (FormatException e) {
            throw new BarcodeFormatException(BARCODE_WRONG_FORMAT_MESSAGE, e);
        }
    }
}
