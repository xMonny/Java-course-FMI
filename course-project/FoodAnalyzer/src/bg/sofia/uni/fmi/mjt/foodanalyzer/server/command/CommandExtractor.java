package bg.sofia.uni.fmi.mjt.foodanalyzer.server.command;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.storage.FoodStorageAccessor;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.CommandPattern.DISCONNECT_COMMAND;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.CommandPattern.FOOD_BY_BARCODE_NO_NUTRIENTS_COMMAND;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.CommandPattern.FOOD_BY_BARCODE_WITH_NUTRIENTS_COMMAND;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.CommandPattern.FOOD_BY_FDC_ID_NO_NUTRIENTS_COMMAND;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.CommandPattern.FOOD_BY_FDC_ID_WITH_NUTRIENTS_COMMAND;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.CommandPattern.FOOD_BY_FOOD_CODE_NO_NUTRIENTS_COMMAND;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.CommandPattern.FOOD_BY_FOOD_CODE_WITH_NUTRIENTS_COMMAND;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.CommandPattern.FOOD_BY_IMAGE_BARCODE_NO_NUTRIENTS_COMMAND;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.CommandPattern.FOOD_BY_IMAGE_BARCODE_WITH_NUTRIENTS_COMMAND;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.CommandPattern.FOOD_BY_IMAGE_NO_NUTRIENTS_COMMAND;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.CommandPattern.FOOD_BY_IMAGE_WITH_NUTRIENTS_COMMAND;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.CommandPattern.FOOD_BY_NAME_NO_NUTRIENTS_COMMAND;
import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.Message.NULL_FOOD_ACCESSOR_MESSAGE;

public final class CommandExtractor {

    private static final String BY_WHITESPACES = "\\s+";
    private static final String BY_EQUAL_SIGN = "=";
    private static final String EMPTY = "";
    private static final String ALL_AFTER_FOUND = " .*";
    private static final String GET_FOOD_SEPARATOR = "^get-food ";

    private static final int AFTER_FIRST_SEPARATOR = 1;
    private static final int AFTER_SECOND_SEPARATOR = 2;

    private final FoodStorageAccessor accessor;
    private final String clientMessage;

    public CommandExtractor(FoodStorageAccessor accessor, String clientMessage) {
        if (accessor == null) {
            throw new IllegalArgumentException(NULL_FOOD_ACCESSOR_MESSAGE);
        }
        this.accessor = accessor;
        this.clientMessage = clientMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CommandExtractor extractor = (CommandExtractor) o;
        return accessor.equals(extractor.accessor)
                && clientMessage.equals(extractor.clientMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessor, clientMessage);
    }

    private ReceiveFoodByNameCommand foodByNameCommand() {
        if (clientMessage != null) {
            if (clientMessage.matches(FOOD_BY_NAME_NO_NUTRIENTS_COMMAND)) {
                String[] messageWords = clientMessage.split(GET_FOOD_SEPARATOR);
                String foodName = messageWords[AFTER_FIRST_SEPARATOR];
                return new ReceiveFoodByNameCommand(accessor, foodName, false);

            }
        }
        return null;
    }

    private ReceiveFoodByBarcodeCommand foodByBarcodeCommand() {
        if (clientMessage != null) {
            String[] messageWords = clientMessage.split(BY_EQUAL_SIGN);

            if (clientMessage.matches(FOOD_BY_BARCODE_NO_NUTRIENTS_COMMAND)) {
                String foodBarcode = messageWords[AFTER_FIRST_SEPARATOR];
                return new ReceiveFoodByBarcodeCommand(accessor, foodBarcode, false);

            } else if (clientMessage.matches(FOOD_BY_BARCODE_WITH_NUTRIENTS_COMMAND)) {
                String foodBarcode = messageWords[AFTER_FIRST_SEPARATOR];
                return new ReceiveFoodByBarcodeCommand(accessor, foodBarcode, true);

            } else if (clientMessage.matches(FOOD_BY_IMAGE_BARCODE_NO_NUTRIENTS_COMMAND)) {
                String foodBarcode = messageWords[AFTER_FIRST_SEPARATOR];
                foodBarcode = foodBarcode.replaceAll(ALL_AFTER_FOUND, EMPTY);
                return new ReceiveFoodByBarcodeCommand(accessor, foodBarcode, false);

            } else if (clientMessage.matches(FOOD_BY_IMAGE_BARCODE_WITH_NUTRIENTS_COMMAND)) {
                String foodBarcode = messageWords[AFTER_FIRST_SEPARATOR];
                foodBarcode = foodBarcode.replaceAll(ALL_AFTER_FOUND, EMPTY);
                return new ReceiveFoodByBarcodeCommand(accessor, foodBarcode, true);
            }
        }
        return null;
    }

    private ReceiveFoodByImageCommand foodByImageCommand() {
        if (clientMessage != null) {
            String[] messageWords = clientMessage.split(BY_EQUAL_SIGN);

            if (clientMessage.matches(FOOD_BY_IMAGE_NO_NUTRIENTS_COMMAND)) {
                String imagePath = messageWords[AFTER_FIRST_SEPARATOR];
                return new ReceiveFoodByImageCommand(accessor, imagePath, false);

            } else if (clientMessage.matches(FOOD_BY_IMAGE_WITH_NUTRIENTS_COMMAND)) {
                String imagePath = messageWords[AFTER_FIRST_SEPARATOR];
                return new ReceiveFoodByImageCommand(accessor, imagePath, true);
            }
        }
        return null;
    }

    private ReceiveFoodByFoodCodeCommand foodByFoodCodeCommand() {
        if (clientMessage != null) {
            String[] messageWords = clientMessage.split(BY_WHITESPACES);
            if (clientMessage.matches(FOOD_BY_FOOD_CODE_NO_NUTRIENTS_COMMAND)) {
                String foodCode = messageWords[AFTER_FIRST_SEPARATOR];
                return new ReceiveFoodByFoodCodeCommand(accessor, foodCode, false);

            } else if (clientMessage.matches(FOOD_BY_FOOD_CODE_WITH_NUTRIENTS_COMMAND)) {
                String foodCode = messageWords[AFTER_SECOND_SEPARATOR];
                return new ReceiveFoodByFoodCodeCommand(accessor, foodCode, true);
            }
        }
        return null;
    }

    private ReceiveFoodByReportCommand foodByReportCommand() {
        if (clientMessage != null) {
            String[] messageWords = clientMessage.split(BY_WHITESPACES);
            if (clientMessage.matches(FOOD_BY_FDC_ID_NO_NUTRIENTS_COMMAND)) {
                String foodFdcId = messageWords[AFTER_FIRST_SEPARATOR];
                return new ReceiveFoodByReportCommand(accessor, foodFdcId, false);

            } else if (clientMessage.matches(FOOD_BY_FDC_ID_WITH_NUTRIENTS_COMMAND)) {
                String foodFdcId = messageWords[AFTER_SECOND_SEPARATOR];
                return new ReceiveFoodByReportCommand(accessor, foodFdcId, true);
            }
        }
        return null;
    }

    private DisconnectCommand disconnectCommand() {
        if (clientMessage != null) {
            if (clientMessage.matches(DISCONNECT_COMMAND)) {
                return new DisconnectCommand(accessor, "none", false);
            }
        }
        return null;
    }

    public Command extract() {
        Set<Command> commands = new HashSet<>();
        commands.add(foodByNameCommand());
        commands.add(foodByBarcodeCommand());
        commands.add(foodByImageCommand());
        commands.add(foodByFoodCodeCommand());
        commands.add(foodByReportCommand());
        commands.add(disconnectCommand());
        return commands.stream()
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(new InvalidCommand(accessor, "none", false));
    }
}
