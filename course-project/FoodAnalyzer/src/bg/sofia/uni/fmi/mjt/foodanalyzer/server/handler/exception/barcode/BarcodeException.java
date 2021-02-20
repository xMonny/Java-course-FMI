package bg.sofia.uni.fmi.mjt.foodanalyzer.server.handler.exception.barcode;

public class BarcodeException extends Exception {

    public BarcodeException(String message) {
        super(message);
    }

    public BarcodeException(String message, Throwable err) {
        super(message, err);
    }
}
