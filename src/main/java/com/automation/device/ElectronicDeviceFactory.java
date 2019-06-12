package com.automation.device;

public class ElectronicDeviceFactory {

    public static ElectronicDevice create(DeviceType type, DeviceState state) {
        switch (type) {
            case AC:
                return new AC(state);
            case LIGHT:
                return new Light(state);
            default:
                throw new IllegalStateException("Invalid device type.");
        }
    }
}