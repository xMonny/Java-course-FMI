package bg.sofia.uni.fmi.mjt.smartcity.hub;

import bg.sofia.uni.fmi.mjt.smartcity.device.SmartDevice;

public class DeviceAlreadyRegisteredException extends RuntimeException {
    public DeviceAlreadyRegisteredException(SmartDevice device) {
        super("Smart device \"" + device.getId() + "\" is already registered");
    }
}
