package com.automation.device;

public abstract class AbstractDevice implements ElectronicDevice {

    protected DeviceState deviceState;

    public AbstractDevice(DeviceState deviceState) {
        this.deviceState = deviceState;
    }

    @Override
    public void turnOn() {
        deviceState = DeviceState.ON;
    }

    @Override
    public void turnOff() {
        deviceState = DeviceState.OFF;
    }

    @Override
    public DeviceState getState() {
        return deviceState;
    }
}