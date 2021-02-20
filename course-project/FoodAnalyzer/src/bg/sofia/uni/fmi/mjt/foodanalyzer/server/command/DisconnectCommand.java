package bg.sofia.uni.fmi.mjt.foodanalyzer.server.command;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.storage.FoodStorageAccessor;

import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.Message.DISCONNECT_MESSAGE;

public final class DisconnectCommand extends Command {

    public DisconnectCommand(FoodStorageAccessor accessor, String key, boolean extraNutrients) {
        super(accessor, key, extraNutrients);
    }

    @Override
    public String getResponse() {
        return DISCONNECT_MESSAGE;
    }
}
