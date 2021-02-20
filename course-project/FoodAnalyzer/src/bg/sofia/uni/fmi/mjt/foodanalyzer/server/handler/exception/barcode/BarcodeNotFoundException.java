package bg.sofia.uni.fmi.mjt.foodanalyzer.server.handler.exception.barcode;

public class BarcodeNotFoundException extends BarcodeException {

    public BarcodeNotFoundException(String message) {
        super(message);
    }

    public BarcodeNotFoundException(String message, Throwable err) {
        super(message, err);
    }
}
