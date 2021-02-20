package bg.sofia.uni.fmi.mjt.foodanalyzer.server.handler.exception.request;

public class FoodException extends Exception {

    public FoodException(String message) {
        super(message);
    }

    public FoodException(String message, Throwable err) {
        super(message, err);
    }
}
