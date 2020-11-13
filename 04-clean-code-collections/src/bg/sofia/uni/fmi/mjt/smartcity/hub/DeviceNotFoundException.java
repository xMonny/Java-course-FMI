package bg.sofia.uni.fmi.mjt.smartcity.hub;

import bg.sofia.uni.fmi.mjt.smartcity.device.SmartDevice;

public class DeviceNotFoundException extends RuntimeException {
    public DeviceNotFoundException(String id) {
        super("Smart device with ID \"" + id + "\" is not found");
    }
    public DeviceNotFoundException(SmartDevice device) {
        super("Smart device with ID \"" + device.getId() + "\" is not found");
    }
}
