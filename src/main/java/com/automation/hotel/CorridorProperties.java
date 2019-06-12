package com.automation.hotel;

import com.automation.device.DeviceState;
import com.automation.device.DeviceType;
import com.automation.util.PropertiesService;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.Map;
import java.util.function.Supplier;

/**
 * The device state and number of devices can be made configurable using this class.
 * Default device state and number of devices can be fetched from Properties file.
 */
public class CorridorProperties {

    private static Table<CorridorType, DeviceType, Integer> deviceCountTable = HashBasedTable.create();
    private static Table<CorridorType, DeviceType, Supplier<DeviceState>> deviceStateTable = HashBasedTable.create();


    //this supplier is used determine state of the device based on time configured in com.automation.util.PropertiesService
    private static Supplier<DeviceState> deviceState = () -> {
        PropertiesService propertiesService = PropertiesService.propertiesService();
        if (!propertiesService.isNightTime()) {
            return DeviceState.OFF;
        }
        return DeviceState.ON;
    };

    static {
        //device counts are hardcoded right now but can also be configured in PropertiesService
        deviceCountTable.put(CorridorType.MAIN, DeviceType.AC, 1);
        deviceCountTable.put(CorridorType.MAIN, DeviceType.LIGHT, 1);
        deviceCountTable.put(CorridorType.SUB, DeviceType.AC, 1);
        deviceCountTable.put(CorridorType.SUB, DeviceType.LIGHT, 1);

        deviceStateTable.put(CorridorType.MAIN, DeviceType.AC, () -> DeviceState.ON);
        deviceStateTable.put(CorridorType.MAIN, DeviceType.LIGHT, deviceState);
        deviceStateTable.put(CorridorType.SUB, DeviceType.AC, () -> DeviceState.ON);
        deviceStateTable.put(CorridorType.SUB, DeviceType.LIGHT, () -> DeviceState.OFF);
    }

    public static Map<DeviceType, Integer> getCountOfDevices(CorridorType type) {
        return deviceCountTable.row(type);
    }

    public static DeviceState getDeviceState(CorridorType corridorType, DeviceType deviceType) {
        Supplier<DeviceState> deviceStateSupplier = deviceStateTable.get(corridorType, deviceType);
        return deviceStateSupplier.get();
    }
}