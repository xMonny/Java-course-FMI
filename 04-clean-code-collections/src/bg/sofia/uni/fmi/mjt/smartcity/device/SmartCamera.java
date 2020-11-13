package bg.sofia.uni.fmi.mjt.smartcity.device;

import bg.sofia.uni.fmi.mjt.smartcity.enums.DeviceType;

import java.time.LocalDateTime;

public final class SmartCamera extends Devices {
    private static long numberSmartCameraDevices = 0;

    public SmartCamera(String name, double powerConsumption, LocalDateTime installationDateTime) {
        super(name, powerConsumption, installationDateTime);
        setDeviceType(DeviceType.CAMERA);
        setId(getTypeShortName() + "-" + getName() + "-" + (numberSmartCameraDevices++));
    }
}
