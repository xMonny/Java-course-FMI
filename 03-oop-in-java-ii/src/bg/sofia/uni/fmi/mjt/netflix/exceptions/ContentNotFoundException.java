package bg.sofia.uni.fmi.mjt.netflix.exceptions;

public class ContentNotFoundException extends RuntimeException {

    public ContentNotFoundException(String videoContentName) {
        super("Content " + "\"" + videoContentName + "\"" + " is not found");
    }
}
