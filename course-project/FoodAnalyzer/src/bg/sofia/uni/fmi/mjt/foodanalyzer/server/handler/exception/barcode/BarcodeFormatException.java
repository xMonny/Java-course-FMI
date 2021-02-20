package bg.sofia.uni.fmi.mjt.foodanalyzer.server.handler.exception.barcode;

public class BarcodeFormatException extends BarcodeException {

    public BarcodeFormatException(String message) {
        super(message);
    }

    public BarcodeFormatException(String message, Throwable err) {
        super(message, err);
    }
}
