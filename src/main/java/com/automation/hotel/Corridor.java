package com.automation.hotel;

import com.automation.device.DeviceState;
import com.automation.device.DeviceType;
import com.automation.device.ElectronicDevice;
import com.automation.device.ElectronicDeviceFactory;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import java.util.List;
import java.util.Map;

public class Corridor {

    private int corridorNumber;
    private int floorNumber;
    private CorridorType type;

    private Multimap<DeviceType, ElectronicDevice> devices;

    private Corridor(int floorNumber, int corridorNumber, CorridorType type) {
        this.corridorNumber = corridorNumber;
        this.type = type;
        this.floorNumber = floorNumber;
        devices = HashMultimap.create();
    }

    public static Corridor createMainCorridor(int floorNumber, int corridorNumber) {
        Corridor corridor = new Corridor(floorNumber, corridorNumber, CorridorType.MAIN);
        addDevices(corridor);
        return corridor;
    }

    public static Corridor createSubCorridor(int floorNumber, int corridorNumber) {
        Corridor corridor = new Corridor(floorNumber, corridorNumber, CorridorType.SUB);
        addDevices(corridor);
        return corridor;
    }

    private static void addDevices(Corridor corridor) {
        Map<DeviceType, Integer> countOfDevices = CorridorProperties.getCountOfDevices(corridor.type);
        for (Map.Entry<DeviceType, Integer> entry : countOfDevices.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                corridor.addDevice(entry.getKey());
            }
        }
    }

    public void addDevice(DeviceType deviceType) {
        DeviceState deviceState = CorridorProperties.getDeviceState(getType(), deviceType);
        devices.put(deviceType, ElectronicDeviceFactory.create(deviceType, deviceState));
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public CorridorType getType() {
        return type;
    }

    public int getCorridorNumber() {
        return corridorNumber;
    }

    public ImmutableMultimap<DeviceType, ElectronicDevice> getDevices() {
        return ImmutableMultimap.copyOf(devices);
    }

    public List<ElectronicDevice> getDevicesByType(DeviceType deviceType) {
        return ImmutableList.copyOf(devices.get(deviceType));
    }

    public int getPowerConsumption() {
        return devices.values().stream()
                .mapToInt(ElectronicDevice::getPowerConsumption)
                .sum();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(type.getName()).append(" corridor ").append(corridorNumber);
        for (DeviceType deviceType : DeviceType.values()) {
            sb.append(appendDeviceTypes(deviceType));
        }
        return sb.toString();
    }

    private String appendDeviceTypes(DeviceType deviceType) {
        List<ElectronicDevice> devicesByType = getDevicesByType(deviceType);
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (ElectronicDevice device : devicesByType) {
            sb.append(" ").append(device.deviceType().name()).append(" ").append(i).append(" : ").append(device.getState().name());
            i++;
        }
        return sb.toString();
    }
}