package bg.sofia.uni.fmi.mjt.foodanalyzer.server.command;

interface Message {

    String NULL_FOOD_ACCESSOR_MESSAGE = "Detected null food accessor";
    String NULL_SEARCH_KEY_MESSAGE = "Detected null search key";

    String INVALID_FOOD_NAME_MESSAGE = "Input name should contain word/words with latin letters";
    String INVALID_FOOD_BARCODE_MESSAGE = "Input barcode should have 12 symbols from 0 to 9";
    String INVALID_FOOD_CODE_MESSAGE =
            "Input food code should have 8 symbols from 0 to 9, starting with symbols from 1 to 9";
    String INVALID_FOOD_FDC_ID_MESSAGE = "Input fdcId should be valid 6-digit number";
    String INVALID_COMMAND_MESSAGE = "Invalid command";

    String FOUND_ZERO_FOODS_MESSAGE = "Found 0 foods from this category";
    String FOOD_PROBLEM_OCCURRED_MESSAGE = "Problem for food occurred: ";
    String IMAGE_BARCODE_PROBLEM_OCCURRED_MESSAGE = "Problem for image barcode occurred: ";

    String INVALID_IMAGE_PATH_MESSAGE = "Invalid image path";
    String INVALID_BARCODE_FROM_IMAGE_MESSAGE = "Invalid barcode from image";
    String NULL_BARCODE_MESSAGE = "Detected null barcode in image";

    String DISCONNECT_MESSAGE = "Disconnected from server";
}
