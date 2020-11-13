package bg.sofia.uni.fmi.mjt.smartcity.device;

import bg.sofia.uni.fmi.mjt.smartcity.enums.DeviceType;

import java.time.LocalDateTime;

public sealed interface SmartDevice permits Devices {
    String getId();
    String getName();
    double getPowerConsumption();
    LocalDateTime getInstallationDateTime();
    DeviceType getType();
}
