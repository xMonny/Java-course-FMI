package bg.sofia.uni.fmi.mjt.smartcity.hub;

import bg.sofia.uni.fmi.mjt.smartcity.device.SmartDevice;
import bg.sofia.uni.fmi.mjt.smartcity.enums.DeviceType;

import java.time.Duration;
import java.time.LocalDateTime;

import java.util.Set;
import java.util.Map;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

public class SmartCityHub {
    private final Map<String, SmartDevice> deviceStorage;

    public SmartCityHub() {
        deviceStorage = new LinkedHashMap<>();
    }

    private boolean isDeviceRegistered(SmartDevice device) {
        return deviceStorage.containsKey(device.getId());
    }

    private boolean isDeviceRegistered(String id) {
        return deviceStorage.containsKey(id);
    }

    public void register(SmartDevice device) throws DeviceAlreadyRegisteredException {
        if (device == null) {
            throw new IllegalArgumentException("Cannot register: Smart device is null");
        }
        if (isDeviceRegistered(device)) {
            throw new DeviceAlreadyRegisteredException(device);
        }
        deviceStorage.put(device.getId(), device);
    }

    public void unregister(SmartDevice device) throws DeviceNotFoundException {
        if (device == null) {
            throw new IllegalArgumentException("Cannot unregister: Smart device is null");
        }
        if (!isDeviceRegistered(device)) {
            throw new DeviceNotFoundException(device);
        }
        deviceStorage.remove(device.getId());
    }

    public SmartDevice getDeviceById(String id) throws DeviceNotFoundException {
        if (id == null) {
            throw new IllegalArgumentException("Cannot find device by ID: ID is null");
        }
        if (!isDeviceRegistered(id)) {
            throw new DeviceNotFoundException(id);
        }
        return deviceStorage.get(id);
    }

    public int getDeviceQuantityPerType(DeviceType type) {
        if (type == null) {
            throw new IllegalArgumentException("Cannot get quantity by type: Type is null");
        }
        int quantity = 0;
        for (SmartDevice d: deviceStorage.values()) {
            if (d.getType().equals(type)) {
                quantity++;
            }
        }
        return quantity;
    }

    private final Comparator<SmartDevice> devicePowerConsumptionComparator = new Comparator<SmartDevice>() {
        final LocalDateTime currentTime = LocalDateTime.now();
        @Override
        public int compare(SmartDevice o1, SmartDevice o2) {
            LocalDateTime installationDateTime1 = o1.getInstallationDateTime();
            LocalDateTime installationDateTime2 = o2.getInstallationDateTime();
            long allHours1 = Duration.between(currentTime, installationDateTime1).toHours();
            long allHours2 = Duration.between(currentTime, installationDateTime2).toHours();
            double powerConsumption1 = o1.getPowerConsumption();
            double powerConsumption2 = o2.getPowerConsumption();

            double allPowerConsumption1 = allHours1*powerConsumption1;
            double allPowerConsumption2 = allHours2*powerConsumption2;

            if (allPowerConsumption1 < allPowerConsumption2) {
                return -1;
            }
            if (allPowerConsumption1 > allPowerConsumption2) {
                return 1;
            }

            return 0;
        }
    };

    public Collection<String> getTopNDevicesByPowerConsumption(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Cannot get top devices by power consumption: " + n + " is negative");
        }

        List<SmartDevice> sortedDeviceStorage = new ArrayList<>(deviceStorage.values());
        sortedDeviceStorage.sort(devicePowerConsumptionComparator);

        List<String> getNTopDevicesID = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            SmartDevice currentSmartDevice = sortedDeviceStorage.get(i);
            getNTopDevicesID.add(currentSmartDevice.getId());
            if (i == deviceStorage.size() - 1) {
                break;
            }
        }
        return getNTopDevicesID;
    }

    public Collection<SmartDevice> getFirstNDevicesByRegistration(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Cannot get top devices by registration: " + n + " is negative");
        }

        if (n >= deviceStorage.size()) {
            return deviceStorage.values();
        }

        Set<SmartDevice> getTopNRegisteredDevices = new LinkedHashSet<>();
        int i = 0;
        for (SmartDevice d: deviceStorage.values()) {
            getTopNRegisteredDevices.add(d);
            i++;
            if (i == n) {
                break;
            }
        }
        return getTopNRegisteredDevices;
    }
}
