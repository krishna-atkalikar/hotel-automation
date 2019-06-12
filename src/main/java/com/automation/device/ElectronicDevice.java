package com.automation.device;

/**
 * Interface that represents electronic device.
 */
public interface ElectronicDevice {

    void turnOn();

    void turnOff();

    DeviceState getState();

    int getPowerConsumption();

    DeviceType deviceType();
}