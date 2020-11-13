package bg.sofia.uni.fmi.mjt.smartcity.device;

import bg.sofia.uni.fmi.mjt.smartcity.enums.DeviceType;

import java.time.LocalDateTime;
import java.util.Objects;

public sealed abstract class Devices implements SmartDevice permits SmartTrafficLight, SmartLamp, SmartCamera{
    private final String name;
    private DeviceType deviceType;
    private String id;
    private final double powerConsumption;
    private final LocalDateTime installationDateTime;

    public Devices(String name, double powerConsumption, LocalDateTime installationDateTime) {
        this.name = name;
        this.powerConsumption = powerConsumption;
        this.installationDateTime = installationDateTime;
    }

    protected void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    protected void setId(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public double getPowerConsumption() {
        return this.powerConsumption;
    }

    @Override
    public LocalDateTime getInstallationDateTime() {
        return this.installationDateTime;
    }

    @Override
    public DeviceType getType() {
        return this.deviceType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Devices)) return false;
        Devices devices = (Devices) o;
        return getId().equals(devices.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    protected String getTypeShortName() {
        return this.deviceType.getShortName();
    }

    public void print() {
        System.out.println("Type: " + getType());
        System.out.println("Short name: " + getTypeShortName());
        System.out.println("Given name: " + getId());
        System.out.println("Power consumption: " + getPowerConsumption());
        System.out.println("Installation time: " + getInstallationDateTime());
        System.out.println();
    }
}
