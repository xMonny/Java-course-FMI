package bg.sofia.uni.fmi.mjt.socialmedia.content.enums;

public enum ContentType {
    POST("post"),
    STORY("story");

    private final String contentNameType;

    ContentType(String contentNameType) {
        this.contentNameType = contentNameType;
    }

    public String getContentNameType() {
        return this.contentNameType;
    }
}
