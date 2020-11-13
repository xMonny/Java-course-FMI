package bg.sofia.uni.fmi.mjt.smartcity.device;

import bg.sofia.uni.fmi.mjt.smartcity.enums.DeviceType;

import java.time.LocalDateTime;

public final class SmartLamp extends Devices {
    private static long numberSmartLampDevices = 0;

    public SmartLamp(String name, double powerConsumption, LocalDateTime installationDateTime) {
        super(name, powerConsumption, installationDateTime);
        setDeviceType(DeviceType.LAMP);
        setId(getTypeShortName() + "-" + getName() + "-" + (numberSmartLampDevices++));
    }
}
