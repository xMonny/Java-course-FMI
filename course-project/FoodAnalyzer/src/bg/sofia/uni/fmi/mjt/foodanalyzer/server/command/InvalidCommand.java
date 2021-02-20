package bg.sofia.uni.fmi.mjt.foodanalyzer.server.command;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.storage.FoodStorageAccessor;

import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.Message.INVALID_COMMAND_MESSAGE;

public final class InvalidCommand extends Command {

    public InvalidCommand(FoodStorageAccessor accessor, String key, boolean extraNutrients) {
        super(accessor, key, extraNutrients);
    }

    @Override
    public String getResponse() {
        return INVALID_COMMAND_MESSAGE;
    }
}
