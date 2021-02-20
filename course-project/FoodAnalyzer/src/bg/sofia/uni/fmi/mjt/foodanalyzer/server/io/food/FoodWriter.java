package bg.sofia.uni.fmi.mjt.foodanalyzer.server.io.food;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.io.stream.AppendingObjectOutputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public final class FoodWriter {

    private static final String FILE_CLOSING_ERROR_MESSAGE = "Error occurred while closing file";

    private final String fileName;

    public FoodWriter(String fileName) {
        if (fileName == null) {
            throw new IllegalArgumentException("Detected null file name in input");
        }
        this.fileName = fileName;
    }

    private void closeObjectOutputStream(ObjectOutputStream ous) {
        if (ous != null) {
            try {
                ous.close();
            } catch (IOException e) {
                throw new IllegalStateException(FILE_CLOSING_ERROR_MESSAGE, e);
            }
        }
    }

    public void write(Object object) throws IOException {
        File file = new File(fileName);

        FileOutputStream fileOutputStream = new FileOutputStream(file, true);

        ObjectOutputStream objectOutputStream;
        if (file.length() == 0) {
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
        } else {
            objectOutputStream = new AppendingObjectOutputStream(fileOutputStream);
        }

        objectOutputStream.writeObject(object);
        objectOutputStream.flush();

        closeObjectOutputStream(objectOutputStream);
    }
}
