package com.automation.device;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LightTest {

    @Test
    public void turnOff_changesStateToOff() {
        Light light = new Light(DeviceState.ON);

        light.turnOff();

        assertEquals(DeviceState.OFF, light.getState());
    }

    @Test
    public void turnOn_changesDeviceStateToOn() {
        Light light = new Light(DeviceState.OFF);

        light.turnOn();

        assertEquals(DeviceState.ON, light.getState());
    }

    @Test
    public void getPowerConsumption_returnsZeroIfDeviceIsOFF() {
        Light light = new Light(DeviceState.OFF);

        assertEquals(0, light.getPowerConsumption());
    }

    @Test
    public void getPowerConsumption_returnsZeroIfDeviceIsON() {
        Light light = new Light(DeviceState.ON);

        assertEquals(5, light.getPowerConsumption());
    }

    @Test
    public void deviceType_returnAC() {
        Light light = new Light(DeviceState.OFF);

        assertEquals(DeviceType.LIGHT, light.deviceType());
    }
}