package bg.sofia.uni.fmi.mjt.foodanalyzer.server.handler.exception.request;

public class FoodNotFoundException extends FoodException {

    public FoodNotFoundException(String message) {
        super(message);
    }

    public FoodNotFoundException(String message, Throwable err) {
        super(message, err);
    }
}
