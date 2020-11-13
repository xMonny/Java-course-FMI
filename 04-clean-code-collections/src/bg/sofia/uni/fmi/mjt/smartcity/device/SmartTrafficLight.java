package bg.sofia.uni.fmi.mjt.smartcity.device;

import bg.sofia.uni.fmi.mjt.smartcity.enums.DeviceType;

import java.time.LocalDateTime;

public final class SmartTrafficLight extends Devices {
    private static long numberSmartTrafficLightDevices = 0;

    public SmartTrafficLight(String name, double powerConsumption, LocalDateTime installationDateTime) {
        super(name, powerConsumption, installationDateTime);
        setDeviceType(DeviceType.TRAFFIC_LIGHT);
        setId(getTypeShortName() + "-" + getName() + "-" + (numberSmartTrafficLightDevices++));
    }
}
