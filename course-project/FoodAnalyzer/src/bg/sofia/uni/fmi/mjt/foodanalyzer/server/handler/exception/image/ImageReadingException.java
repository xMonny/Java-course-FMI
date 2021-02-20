package bg.sofia.uni.fmi.mjt.foodanalyzer.server.handler.exception.image;

public class ImageReadingException extends Exception {

    public ImageReadingException(String message) {
        super(message);
    }

    public ImageReadingException(String message, Throwable err) {
        super(message, err);
    }
}
