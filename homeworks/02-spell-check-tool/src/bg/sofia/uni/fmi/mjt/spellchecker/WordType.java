package bg.sofia.uni.fmi.mjt.spellchecker;

public enum WordType {
    DICTIONARY_WORD("Dictionary"),
    STOP_WORD("StopWord");

    private final String typeName;

    WordType(String typeName) {
        this.typeName = typeName;
    }

    public boolean isDictionaryWordType() {
        return typeName.equals("Dictionary");
    }
}
