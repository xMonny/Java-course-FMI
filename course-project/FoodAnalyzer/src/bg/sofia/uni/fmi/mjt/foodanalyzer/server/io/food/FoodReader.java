package bg.sofia.uni.fmi.mjt.foodanalyzer.server.io.food;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.Food;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashSet;
import java.util.Set;

public final class FoodReader {

    private static final String FILE_CLOSING_ERROR_MESSAGE = "Error occurred while closing file";
    private static final String NULL_FILE_NAME = "Detected null file name in input";

    private final String fileName;

    public FoodReader(String fileName) {
        if (fileName == null) {
            throw new IllegalArgumentException(NULL_FILE_NAME);
        }
        this.fileName = fileName;
    }

    private void closeObjectInputStream(ObjectInputStream ois) {
        if (ois != null) {
            try {
                ois.close();
            } catch (IOException e) {
                System.err.println(FILE_CLOSING_ERROR_MESSAGE);
            }
        }
    }

    public Set<Object> read() throws IOException, ClassNotFoundException {
        FileInputStream inputStream = new FileInputStream(fileName);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        Set<Object> readFoods = new HashSet<>();
        while (inputStream.available() > 0) {
            Object currentObject = objectInputStream.readObject();
            if (currentObject instanceof Food) {
                readFoods.add(currentObject);
            }
        }

        closeObjectInputStream(objectInputStream);

        return readFoods;
    }
}
